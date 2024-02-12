LICENSE = "CLOSED"

require gxp2-bootblock.inc

HPE_GXP_KEY_FILES_DIR = "${COREBASE}/meta-proliant-bsp/recipes-bsp/image/gxp2-bootblock"

BBRANCH = "gxp2-bootblock"

SRC_URI = "git://github.com/HewlettPackard/gxp-bootblock.git;branch=${BBRANCH};protocol=https"

SRCREV = "1714c07e0f6a3ab3888d474e49b818551c09bd93"

S = "${WORKDIR}/git"

inherit deploy

do_deploy () {
  install -d ${DEPLOYDIR}

  # Copy in files from the files subdirectory
  install -m 644 ${HPE_GXP_KEY_FILES_DIR}/${HPE_UBOOT_SIGNING_HEADER_512} ${DEPLOYDIR}

  # Copy in the U-Boot signing key
  install -m 644 ${HPE_GXP_KEY_FILES_DIR}/${HPE_UBOOT_SIGNING_KEY} ${DEPLOYDIR}

  # Copy in the bootblock
  install -m 644 GXP2loader-t277-t280-t285-sgn00.bin ${DEPLOYDIR}/${HPE_GXP_BOOTBLOCK_IMAGE}
  #install -m 644 GXP2loader-t26x-sgn00.bin ${DEPLOYDIR}/${HPE_GXP_BOOTBLOCK_IMAGE}
}

addtask deploy before do_build after do_compile

