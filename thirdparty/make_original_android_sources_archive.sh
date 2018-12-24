#!/bin/sh

android_home=$HOME/sys/src/git/android_platform_frameworks

android_srcpath=$android_home/core/java

files="\
android/annotation/Nullable.java \
android/app/SharedPreferencesImpl.java \
android/content/SharedPreferences.java \
android/util/Base64.java \
android/util/AttributeSet.java \
android/util/XmlPullAttributes.java \
android/util/Xml.java \
com/android/internal/util/XmlUtils.java"


cwd=$PWD
archive="original_android_sources"

if [ -e $archive.tar.xz ]; then
	suffix=$(date +%Y%m%d%H%M)
	mv $archive.tar.xz $archive.tar.xz.bak.$suffix
fi

cd $android_srcpath

tar -Jcvf $cwd/$archive.tar.xz $files

cd $cwd
