package com.xachin.test.storage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import com.xachin.test.storage.databinding.ActivityMainBinding
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

}
