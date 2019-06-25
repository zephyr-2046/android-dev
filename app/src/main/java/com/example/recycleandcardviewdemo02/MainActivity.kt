package com.example.recycleandcardviewdemo02

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {

    // Initializing an empty ArrayList to be filled with animals
    val animals: ArrayList<String> = ArrayList()

    // Adds animals to the empty animals ArrayList
    fun addAnimals() {
        animals.add("dog")
        animals.add("cat")
        animals.add("owl")
        animals.add("cheetah")
        animals.add("raccoon")
        animals.add("bird")
        animals.add("snake")
        animals.add("lizard")
        animals.add("hamster")
        animals.add("bear")
        animals.add("lion")
        animals.add("tiger")
        animals.add("horse")
        animals.add("frog")
        animals.add("fish")
        animals.add("shark")
        animals.add("turtle")
        animals.add("elephant")
        animals.add("cow")
        animals.add("beaver")
        animals.add("bison")
        animals.add("porcupine")
        animals.add("rat")
        animals.add("mouse")
        animals.add("goose")
        animals.add("deer")
        animals.add("fox")
        animals.add("moose")
        animals.add("buffalo")
        animals.add("monkey")
        animals.add("penguin")
        animals.add("parrot")
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        when (intent.action) {

            "android.intent.action.MAIN" ->
                Log.d("zephyr", "action is main")
            "android.intent.action.SEND" -> {

                Log.d("zephyr", "action is send")
                Log.d("zephyr", "data " + intent.clipData?.itemCount)

                if (intent.clipData != null) { // handle multiple photo

                    for (i in 0 until (intent.clipData?.itemCount ?: 0)) {
                        val uri = intent.clipData?.getItemAt(i)?.uri

                        Log.d("zephyr", "uri " + uri)

                        if (uri != null) {
                            importPhoto(this, uri)
                        }
                    }
                }
            }

            else ->
                Log.d("zephyr", "action is unknown")
        }

        if (intent != null) {

            Log.d("zephyr", "intent data is NOT null" + intent)

        } else {
            Log.d("zephyr", "intent data is null")
        }

        // Loads animals into the ArrayList
        addAnimals()

        // Creates a vertical Layout Manager
        rv_animal_list.layoutManager = LinearLayoutManager(this)

        // You can use GridLayoutManager if you want multiple columns. Enter the number of columns as a parameter.
        // rv_animal_list.layoutManager = GridLayoutManager(this, 2)

        // Access the RecyclerView Adapter and load the data into it
        rv_animal_list.adapter = AnimalAdapter(animals, this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()

        val result = Unit3A().calculateSum(12, 11)

        textPrompt.setText("the result is " + result)
    }

    fun isImage(context: Context, uri: Uri): Boolean {
        val mimeType = context.contentResolver.getType(uri) ?: return true
        return mimeType.startsWith("image/")
    }

    private fun createImageFile(dir: File, fileName: String? = null): File {

        if (fileName == null) {
            val timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-kkmmss"))
            return File.createTempFile("IMG_$timestamp", ".jpg", dir)
        }

        return File(dir, fileName)
    }

    fun copyUriToFile(context: Context, uri: Uri, outputFile: File) {

        val inputStream = context.contentResolver.openInputStream(uri)

        // copy inputStream to file using okio
        /*
        val source = Okio.buffer(Okio.source(inputStream))
        val sink = Okio.buffer(Okio.sink(outputFile))

        sink.writeAll(source)

        sink.close()
        source.close()
         */

        val outputStream = FileOutputStream(outputFile)
        inputStream.use { input ->
            outputStream.use { output ->
                input?.copyTo(output)
            }
        }
    }

    fun importPhoto(context: Context, uri: Uri): Boolean {

        Log.d("zephyr", "importPhoto() ")

        if (!isImage(context, uri)) {
            // not image
            return false
        }

        return try {

            val photoFile = createImageFile(context.cacheDir)

            Log.d("zephyr", "photoFile = " + photoFile)

            copyUriToFile(context, uri, photoFile)

            // addImageToGallery(photoFile)
            true
        } catch (e: IOException) {
            e.printStackTrace()
            // handle error
            false
        }
    }
}