import de.geolykt.starplane.StarloaderStarplaneProject;
import de.geolykt.starplane.StarplaneProject;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import io.github.coolcrabs.brachyura.maven.MavenId;
import io.github.coolcrabs.brachyura.project.DescriptiveBuildscriptName;

public class Buildscript extends StarloaderStarplaneProject implements DescriptiveBuildscriptName {

    public Buildscript() {
        super(new MavenId("de.geolykt", "starloader-api", "2.0.0"),
                StarplaneProject.newProjectConfig()
                    .removeTransitiveDependency("com.google.code.findbugs", "jsr305")
                    .removeTransitiveDependency("com.google.errorprone", "error_prone_annotations")
                    .removeTransitiveDependency("com.google.j2objc", "j2objc-annotations")
                    .removeTransitiveDependency("org.checkerframework", "checker-qual")
                    .addHttpRepository("https://geolykt.de/maven", false) // FIXME fix this at the source
                    .addHttpRepository("https://repo1.maven.org/maven2/")
                    .setAccessWidenerFile("starloader-api.accesswidener")
                    .addDependency("org.ow2.asm", "asm-analysis", "9.2", "https://asm.ow2.io/javadoc/")
                    .addDependency("org.ow2.asm", "asm-commons", "9.2", "https://asm.ow2.io/javadoc/")
                    .addDependency("org.ow2.asm", "asm-util", "9.2", "https://asm.ow2.io/javadoc/")
                    .addDependency("org.ow2.asm", "asm-tree", "9.2", "https://asm.ow2.io/javadoc/")
                    .addDependency("org.ow2.asm", "asm", "9.2", "https://asm.ow2.io/javadoc/")
                    .addDependency("org.jetbrains", "annotations", "22.0.0", "https://javadoc.io/doc/org.jetbrains/annotations/22.0.0/")
                    .addDependency("de.geolykt.starloader", "mixin", "0.8.4", "https://jenkins.liteloader.com/view/Other/job/Mixin/javadoc/")
                    .addDependency("com.badlogicgames.gdx", "gdx", "1.9.11", "https://libgdx.badlogicgames.com/ci/nightlies/docs/api/")
                    .addDependency("de.geolykt.starloader", "launcher", "3.0.0"));

        this.getCompileOptions()
            .addRawOption("-Xlint:all")
            .addRawOption("-Xlint:-deprecation")
            .addRawOption("-Xlint:-removal")
            .addRawOption("-Xlint:-unchecked")
            .addRawOption("-Xlint:-classfile");
    }

    @Override
    @NotNull
    @Contract(pure = true)
    public String getBuildscriptName() {
        return "slapiBuildscript";
    }

    static {
        System.setProperty("de.geolykt.starplane.nocache", "true");
    }
}
