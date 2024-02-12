LICENSE = "CLOSED"

HPE_GXP_FITIMAGE_DIR = "${COREBASE}/meta-proliant-bsp/recipes-bsp/image/gxp2-fitimage"

inherit deploy

# DEPENDS:append = " gxp2-fitimage u-boot-tools-native dtc-native"
# do_generate_fit_image() {
#     uboot-mkimage -f ${DEPLOY_DIR_IMAGE}/fitimage.its -r ${DEPLOY_DIR_IMAGE}/fitImage-${INITRAMFS_IMAGE_NAME}-${MACHINE}
# }

do_deploy() {
  install -d ${DEPLOYDIR}

  # Copy in files from the files subdirectory
  install -m 644 ${HPE_GXP_FITIMAGE_DIR}/fitimage.its ${DEPLOYDIR}
}

addtask deploy before do_build after do_compile