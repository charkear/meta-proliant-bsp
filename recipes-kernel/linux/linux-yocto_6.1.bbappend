COMPATIBLE_MACHINE:proliant-yocto = "proliant-yocto"


# use supplied defconfig
# KCONFIG_MODE="allnoconfig"
FILESEXTRAPATHS:prepend := "${THISDIR}/linux-yocto:"
SRC_URI += " file://defconfig"

# use for defconfig in tree
# KBUILD_DEFCONFIG="gxp"
