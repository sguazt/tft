.PHONY: all cxx java


all: cxx java

cxx:
	cd $(cxx_srcpath) && $(MAKE) all

java:
	cd $(java_srcpath) && $(MAKE) all

clean:
	cd $(cxx_srcpath) && $(MAKE) clean
	cd $(java_srcpath) && $(MAKE) clean
