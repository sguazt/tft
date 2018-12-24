prj_path=$(CURDIR)

#lib_path := $(prj_path)/lib
thirdparty_path := $(prj_path)/thirdparty
cxx_srcpath := $(prj_path)/src/c++
java_srcpath := $(prj_path)/src/java
#jni_srcpath := $(prj_path)/src/jni
#java_clspath := $(java_srcpath)
##java_clspath := $(java_clspath):$(thirdparty_path)/sqlite-jdbc-3.16.1.jar
##java_clspath := $(java_clspath):$(thirdparty_path)/commons-cli-1.3.1.jar
##java_clspath := $(java_clspath):$(thirdparty_path)/commons-lang3-3.5.jar
#java_clspath := $(java_clspath)$(patsubst %,:%,$(wildcard $(thirdpaty_path)/*.jar))

CXXFLAGS+=-Wall -Wextra -std=c++11 -pedantic
CXXFLAGS+=-O0 -g
#CXXFLAGS+=-DNDEBUG


export prj_path cxx_srcpath java_srcpath thirdparty_path
export CXXFLAGS


.PHONY: all clean


all:
	cd src && $(MAKE)

clean:
	cd src && $(MAKE) clean
