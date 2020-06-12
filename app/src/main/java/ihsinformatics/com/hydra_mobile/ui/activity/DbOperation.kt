package ihsinformatics.com.hydra_mobile.ui.activity

import android.app.Activity
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.Toast
import ihsinformatics.com.hydra_mobile.R
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.channels.FileChannel


class DbOperation : Activity() {

    private lateinit var  importButton: Button
    private lateinit var  exportButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_db_operation)

        importButton=findViewById<Button>(R.id.importOper)
        exportButton=findViewById<Button>(R.id.exportOper)


        val direct = File(Environment.getExternalStorageDirectory().toString() + "/BackupFolder")

        if (!direct.exists()) {
            if (direct.mkdir()) {
                //directory is created;
            }
        }

        importButton.setOnClickListener{
            importDB()
        }

        exportButton.setOnClickListener{
            exportDB()
        }

    }

    private fun importDB() {
        // TODO Auto-generated method stub
        try {
            val sd = Environment.getExternalStorageDirectory()
            val data = Environment.getDataDirectory()
            if (sd.canWrite()) {
                val currentDBPath = "//data//" + "ihsinformatics.com.hydra_mobile" + "//databases//" + "greendao_demo.db"
                val backupDBPath = "/BackupFolder/greendao_demo.db"
                val backupDB = File(data, currentDBPath)
                val currentDB = File(sd, backupDBPath)
                val src: FileChannel = FileInputStream(currentDB).getChannel()
                val dst: FileChannel = FileOutputStream(backupDB).getChannel()
                dst.transferFrom(src, 0, src.size())
                src.close()
                dst.close()
                Toast.makeText(baseContext, backupDB.toString(), Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            Toast.makeText(baseContext, e.toString(), Toast.LENGTH_LONG).show()
        }
    }

    //exporting database
    private fun exportDB() {
        // TODO Auto-generated method stub
        try {
            val sd = Environment.getExternalStorageDirectory()
            val data = Environment.getDataDirectory()
            if (sd.canWrite()) {
                val currentDBPath = "//data//" + "ihsinformatics.com.hydra_mobile" + "//databases//" + "greendao_demo.db"
                val backupDBPath = "/BackupFolder/greendao_demo.db"
                val currentDB = File(data, currentDBPath)
                val backupDB = File(sd, backupDBPath)
                val src = FileInputStream(currentDB).channel
                val dst = FileOutputStream(backupDB).channel
                dst.transferFrom(src, 0, src.size())
                src.close()
                dst.close()
                Toast.makeText(baseContext, backupDB.toString(), Toast.LENGTH_LONG).show()
            }
        } catch (e: java.lang.Exception) {
            Toast.makeText(baseContext, e.toString(), Toast.LENGTH_LONG).show()
        }
    }
}
