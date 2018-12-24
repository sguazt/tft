#nullstring :=
#space := $(nullstring)
space :=
space +=
#jars := $(patsubst %,:%,$(wildcard $(thirdparty_path)/*.jar))
#jars := $(subst $(space),,$(jars))
java_clspath := $(java_srcpath)
#java_clspath := $(java_clspath):$(thirdparty_path)/sqlite-jdbc-3.16.1.jar
#java_clspath := $(java_clspath):$(thirdparty_path)/commons-cli-1.3.1.jar
#java_clspath := $(java_clspath):$(thirdparty_path)/commons-lang3-3.5.jar
java_clspath := $(java_clspath)$(subst $(space):,:,$(patsubst %,:%,$(wildcard $(thirdparty_path)/*.jar)))

#tgnet_objects := $(patsubst %.cpp,%.o,$(wildcard $(jni_srcpath)/tgnet/*.cpp))
#test_objects := $(patsubst %.java,%.class,$(wildcard $(prj_path)/test/java/*.java))


.PHONY: all clean db_deserializer test tlrpc_tag_dumper utils


all: db_deserializer tlrpc_tag_dumper usrcfg_deserializer utils

db_deserializer: $(java_srcpath)/tft/android/DatabaseDeserializer.class

tlrpc_tag_dumper: $(java_srcpath)/tft/android/TLRPCTagDumper.class

usrcfg_deserializer: $(java_srcpath)/tft/android/UserConfigDeserializer.class

utils: $(java_srcpath)/tft/util/JavaPropertiesPrinter.class

#test: $(test_objects)

clean:
	$(RM) $(java_srcpath)/android/annotation/*.class
	$(RM) $(java_srcpath)/android/app/*.class
	$(RM) $(java_srcpath)/android/content/*.class
	$(RM) $(java_srcpath)/android/util/*.class
	$(RM) $(java_srcpath)/android/*.class
	$(RM) $(java_srcpath)/com/android/internal/util/*.class
	$(RM) $(java_srcpath)/com/android/internal/*.class
	$(RM) $(java_srcpath)/com/android/*.class
	$(RM) $(java_srcpath)/com/*.class
	$(RM) $(java_srcpath)/tft/android/tgnet/*.class
	$(RM) $(java_srcpath)/tft/android/*.class
	$(RM) $(java_srcpath)/tft/util/*.class
	$(RM) $(java_srcpath)/tft/*.class
	$(RM) $(java_srcpath)/org/telegram/tgnet/*.class
	$(RM) $(java_srcpath)/org/telegram/messenger/time/*.class
	$(RM) $(java_srcpath)/org/telegram/messenger/*.class
	$(RM) $(java_srcpath)/org/telegram/*.class
	$(RM) $(java_srcpath)/org/*.class
	$(RM) $(test_objects)

%.class: %.java
	javac -classpath $(java_clspath) $^
