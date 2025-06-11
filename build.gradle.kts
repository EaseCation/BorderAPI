plugins {
    id("ecbuild.java-conventions")
    id("ecbuild.copy-conventions")
}

extra.set("copyTo", "{server}/plugins")

dependencies {
    compileOnly(project(":Nukkit"))
}

description = "BorderAPI"
