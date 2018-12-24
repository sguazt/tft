#!/bin/sh

tg_home=$HOME/sys/src/git/TelegramAndroid

tg_srcpath=$tg_home/TMessagesProj/src/main/java

files="\
org/telegram/messenger/ApplicationLoader.java \
org/telegram/messenger/BuildVars.java \
org/telegram/messenger/FileLog.java \
org/telegram/messenger/UserConfig.java \
org/telegram/messenger/Utilities.java \
org/telegram/messenger/time/DateParser.java \
org/telegram/messenger/time/DatePrinter.java \
org/telegram/messenger/time/FormatCache.java \
org/telegram/messenger/time/FastDateFormat.java \
org/telegram/messenger/time/FastDateParser.java \
org/telegram/messenger/time/FastDatePrinter.java \
org/telegram/tgnet/AbstractSerializedData.java \
org/telegram/tgnet/NativeByteBuffer.java \
org/telegram/tgnet/SerializedData.java \
org/telegram/tgnet/TLObject.java \
org/telegram/tgnet/TLRPC.java"

cwd=$PWD
archive="original_org_telegram_sources"

if [ -e $archive.tar.xz ]; then
	suffix=$(date +%Y%m%d%H%M)
	mv $archive.tar.xz $archive.tar.xz.bak.$suffix
fi

cd $tg_srcpath

tar -Jcvf $cwd/$archive.tar.xz $files

cd $cwd
