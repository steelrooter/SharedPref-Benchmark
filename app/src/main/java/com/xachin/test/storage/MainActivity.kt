package com.xachin.test.storage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import com.xachin.test.storage.databinding.ActivityMainBinding
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.rxkotlin.Observables
import io.reactivex.schedulers.Schedulers
import timber.log.Timber


class MainActivity :
    AppCompatActivity(),
    BenchmarkViewModel {

    private val fileName: String
        get() = fileField.text.toString().let { if (it.isBlank()) "sp_benchmark" else it }

    private val numberOfPairs: Long
        get() = try {
            numberField.text.toString().toLong()
        } catch (e: Throwable) {
            1_000_000
        }

    private lateinit var binding: ActivityMainBinding
    private val fileField: EditText
        get() = binding.fileField
    private val numberField: EditText
        get() = binding.numberField

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.vm = this
    }

    override fun onClickInit() {
        withExecutionTimeLog("SP init") {
            SharedPrefHelper.init(this, fileName)
        }
    }

    override fun onClickGenerate() {
        withExecutionTimeLog("Generate random data") {
            SharedPrefHelper.generateRandomData(numberOfPairs)
        }
    }

    override fun onClickWrite() {
        withExecutionTimeLog("Write data") {
            SharedPrefHelper.writeDataToDisk(fileName)
        }
    }

    override fun onClickRead() {
        withExecutionTimeLog("Read data") {
            SharedPrefHelper.readAllDataFromDisk(numberOfPairs, fileName) {
                Timber.d("~~~~ First read took ${now() - it}ms")
            }
        }
    }

    override fun onClickInitAndRead() {
        withExecutionTimeLog("SP init") {
            SharedPrefHelper.init(this, fileName)
        }
        withExecutionTimeLog("Read data") {
            SharedPrefHelper.readAllDataFromDisk(numberOfPairs, fileName) {
                Timber.d("~~~~ First read took ${now() - it}ms")
            }
        }
    }

    override fun onClickMultiInitAndRead() {
        val bigFile = Observable.fromCallable {
            initAndRead("sp_benchmark", 10)
        }.subscribeOn(Schedulers.io())

        val smallFile = Observable.fromCallable {
            initAndRead("t", 10)
        }.subscribeOn(Schedulers.io())

        Observables.zip(bigFile, smallFile)
            .doOnComplete { Timber.d("Multi read completed") }
            .doOnError { Timber.e(it, "Multi read failure") }
            .subscribe()

    }

    private fun initAndRead(file: String, pairs: Long) {
        withExecutionTimeLog("SP init: $file") {
            SharedPrefHelper.init(this, file)
        }
        withExecutionTimeLog("Read data: $file") {
            SharedPrefHelper.readAllDataFromDisk(pairs, file) {
                Timber.d("~~~~ First read from $file took ${now() - it}ms")
            }
        }
    }
}
