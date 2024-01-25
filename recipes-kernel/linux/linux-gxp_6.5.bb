KBRANCH ?= "gxp-upstream"

require recipes-kernel/linux/linux-yocto.inc

SRCREV ?= "2dde18cd1d8fac735875f2e4987f11817cc0bc2c"

SRC_URI = "git://git@github.com/charkear/linux.git;protocol=ssh;branch=${KBRANCH}"

LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"
LINUX_VERSION ?= "6.5"

# use supplied defconfig made with "make ARCH=arm savedefconfig"
FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
SRC_URI += "file://defconfig"
KCONFIG_MODE = "allnoconfig"
 
# use for defconfig in tree
# KBUILD_DEFCONFIG="multi_v7_defconfig"

PV = "${LINUX_VERSION}+git"

KMETA = "kernel-meta"
KCONF_BSP_AUDIT_LEVEL = "1"

COMPATIBLE_MACHINE = "^(proliant-yocto)$"
