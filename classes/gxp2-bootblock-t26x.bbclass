BBRANCH = "gxp2-bootblock"
SRC_URI = "git://github.com/HewlettPackard/gxp-bootblock.git;branch=${BBRANCH};protocol=https"
SRCREV = "1714c07e0f6a3ab3888d474e49b818551c09bd93"
S = "${WORKDIR}/git"

HPE_GXP_KEY_FILES_DIR = "${COREBASE}/meta-proliant-bsp/recipes-bsp/image/files"

inherit deploy

do_deploy () {
  install -d ${DEPLOYDIR}

  # Copy in the bootblock
  install -m 644 GXP2loader-t26x-sgn00.bin ${DEPLOYDIR}/gxp2loader.bin

  # Copy in files from the files subdirectory
  install -m 644 ${HPE_GXP_KEY_FILES_DIR}/hpe-uboot-header-512.section ${DEPLOYDIR}

  # Copy in the U-Boot signing key
  install -m 644 ${HPE_GXP_KEY_FILES_DIR}/hpe-uboot-signing-key.pem ${DEPLOYDIR}

}
