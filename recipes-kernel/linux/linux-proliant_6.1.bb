require recipes-kernel/linux/linux-yocto.inc

SRCREV ?= "d650f111528925b36a30a602529b3fe7788e6505"

KBRANCH:linux_proliant ?= "rl300-linux"
SRCREV_machine:linux_proliant ?= "d650f111528925b36a30a602529b3fe7788e6505"
KERNEL_DEVICETREE:linux-proliant ?= "gxp.dtb"

# use supplied defconfig
# FILESEXTRAPATHS:prepend := "${THISDIR}/linux-proliant:"
# SRC_URI += " file://defconfig"

# use for defconfig in tree
KBUILD_DEFCONFIG="gxp_defconfig"

SRC_URI = "git://git@github.hpe.com/hpe-iop/iop-linux.git;protocol=ssh;branch=${KBRANCH}"

LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"
LINUX_VERSION ?= "6.1.46"

PV = "${LINUX_VERSION}+git"

KMETA = "kernel-meta"
KCONF_BSP_AUDIT_LEVEL = "1"

COMPATIBLE_MACHINE = "^(proliant-yocto)$"