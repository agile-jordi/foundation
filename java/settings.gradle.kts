rootProject.name = "foundation"

fun includeAllIn(vararg folders: String) =
    folders.forEach { folder ->
        File("$rootDir/$folder/")
            .listFiles()!!
            .filter { it.isDirectory }
            .map { it.relativeTo(rootDir) }
            .forEach {
                val name = it.toPath().joinToString(":")
                println("include $name")
                include(name)
            }
    }

includeAllIn("libs", "apps")
