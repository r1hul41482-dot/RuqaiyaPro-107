package com.ruqaiyapro.update
import android.content.Context
import java.io.File
object InAppUpdateManager {
    fun applyCodePatch(ctx: Context, code: String, language: String): String {
        return try{
            val dir = File("/data/data/com.ruqaiyapro/files/patches")
            dir.mkdirs()
            val file = File(dir, "patch_${System.currentTimeMillis()}.${if(language=="python") "py" else "kt"}")
            file.writeText(code)
            // If python, try to interpret simple Feature add
            if(language=="python" && code.contains("Feature")){
                // Example python: add_feature(108, "My New Feature", "desc", "Core")
                // We parse and add to Features107Manager via prefs hack
                val prefs = ctx.getSharedPreferences("custom_features",0)
                prefs.edit().putString("last_patch", code).apply()
            }
            "Patch saved: ${file.absolutePath}\nNow pushing to GitHub..."
        }catch(e:Exception){ "Error: ${e.message}" }
    }
    fun generateGitPushScript(code: String): String {
        return """
        cd ~/downloads/RuqaiyaPro-Working/RuqaiyaPro
        cat > /tmp/new_feature.kt << 'ENDKT'
        $code
        ENDKT
        echo "Patch applied" 
        git add -A
        git commit -m "feat: in-app update - auto patch via app"
        git push
        """.trimIndent()
    }
}
