HOMEPAGE = "http://www.denx.de/wiki/U-Boot/WebHome"
DESCRIPTION = "U-Boot, a boot loader for Embedded boards based on PowerPC, \
ARM, MIPS and several other processors, which can be installed in a boot \
ROM and used to initialize and test the hardware or to download and run \
application code."
SECTION = "bootloaders"
DEPENDS += "flex-native bison-native python3-setuptools-native"

LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=5a7450c57ffe5ae63fd732446b988025"
PE = "1"

BRANCH ?= "v2020.10-gxp-openbmc-tarbal"

# We use the revision in order to avoid having to fetch it from the
# repo during parse
SRCREV = "0a3d0634605ab5212bb9d8fbf929004ad5fe983f"

SRC_URI = "git://git@github.hpe.com/hpe-iop/gxp-uboot.git;protocol=ssh;branch=${BRANCH}"

S = "${WORKDIR}/git"
B = "${WORKDIR}/build"

inherit pkgconfig

do_configure[cleandirs] = "${B}"