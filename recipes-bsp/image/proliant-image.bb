SUMMARY = "Small image capable of booting a ProLiant Server iLO."
DESCRIPTION = "Small image capable of booting a ProLiant Server iLO. The kernel includes \
the Minimal RAM-based Initial Root Filesystem (initramfs), which finds the \
first 'init' program more efficiently."

INITRAMFS_SCRIPTS ?= "\
                      initramfs-framework-base \
                      initramfs-module-setup-live \
                      initramfs-module-udev \
                     "
# removed modules
# initramfs-module-install 
# initramfs-module-install-efi 

PACKAGE_INSTALL = "${INITRAMFS_SCRIPTS} ${VIRTUAL-RUNTIME_base-utils} udev base-passwd ${ROOTFS_BOOTSTRAP_INSTALL} mtd-utils"

# Do not pollute the initrd image with rootfs features
IMAGE_FEATURES = ""

export IMAGE_BASENAME = "${MLPREFIX}proliant-image"
IMAGE_NAME_SUFFIX ?= ""
IMAGE_LINGUAS = ""

LICENSE = "MIT"

IMAGE_FSTYPES = "${INITRAMFS_FSTYPES}"
inherit core-image

IMAGE_ROOTFS_SIZE = "8192"
IMAGE_ROOTFS_EXTRA_SPACE = "0"

# Use the same restriction as initramfs-module-install
COMPATIBLE_HOST = '(x86_64.*|i.86.*|arm.*|aarch64.*|loongarch64.*)-(linux.*|freebsd.*)'

DEPENDS += "gxp-bootblock"

HPE_GXP_BOOTBLOCK_IMAGE ?= "gxp2loader.bin"
HPE_UBOOT_SIGNING_HEADER_512 ?= "hpe-uboot-header-512.section"
HPE_UBOOT_SIGNING_KEY ?= "hpe-uboot-signing-key.pem"

do_generate_rom_image() {
    # Extract uboot 256K
    dd if=/dev/zero bs=1k count=256 > ${DEPLOY_DIR_IMAGE}/u-boot-tmp.bin
    dd bs=1k conv=notrunc seek=0 count=256 \
            if=${DEPLOY_DIR_IMAGE}/u-boot.bin \
            of=${DEPLOY_DIR_IMAGE}/u-boot-tmp.bin

    # TODO - replace this openssl signing command line with whatever command you need to create a
    # digital signature of ${DEPLOY_DIR_IMAGE}/u-boot-tmp.bin

    openssl sha384 -sign ${DEPLOY_DIR_IMAGE}/${HPE_UBOOT_SIGNING_KEY} -out ${DEPLOY_DIR_IMAGE}/gxp-tmp.sig \
        ${DEPLOY_DIR_IMAGE}/u-boot-tmp.bin

    # Cat U-Boot header+signature
    cat ${DEPLOY_DIR_IMAGE}/${HPE_UBOOT_SIGNING_HEADER_512} ${DEPLOY_DIR_IMAGE}/gxp-tmp.sig \
        > ${DEPLOY_DIR_IMAGE}/gxp-uboot.sig

    # Create hpe-section
    dd if=/dev/zero bs=1k count=576 > ${DEPLOY_DIR_IMAGE}/hpe-section

    # Add U-Boot Header and Signature to hpe-section
    dd bs=1k conv=notrunc seek=0 \
        if=${DEPLOY_DIR_IMAGE}/gxp-uboot.sig \
        of=${DEPLOY_DIR_IMAGE}/hpe-section

    # Add gxp-bootblock to hpe-section
    dd bs=1k conv=notrunc seek=64 \
        if=${DEPLOY_DIR_IMAGE}/${HPE_GXP_BOOTBLOCK_IMAGE} \
        of=${DEPLOY_DIR_IMAGE}/hpe-section

    # hpe-section2 is the same as hpe-section up to this point
    cp ${DEPLOY_DIR_IMAGE}/hpe-section ${DEPLOY_DIR_IMAGE}/hpe-section2

    # Expand uboot to 384K
    dd if=/dev/zero bs=1k count=384 > ${DEPLOY_DIR_IMAGE}/u-boot-tmp.bin
    dd bs=1k conv=notrunc seek=0 count=384 \
            if=${DEPLOY_DIR_IMAGE}/u-boot.bin \
            of=${DEPLOY_DIR_IMAGE}/u-boot-tmp.bin

    # Offsets for the POC image and secure boot image
    FLASH_UBOOT_OFFSET="0"
    UBOOT_IMG_SIZE="393216" 
    FLASH_KERNEL_OFFSET="512"
    FLASH_RWFS_OFFSET="29184"
    # Secure boot offsets
    # offset at 0x01f7_0000 / 1024 = 32192
    FLASH_SECTION_OFFSET="32192"
    # end is offset + 576
    FLASH_SECTION_END="32768"
    # offset at 0x01ee_0000 / 1024 = 31616
    FLASH_SECTION2_OFFSET="31616"
    FLASH_SECTION2_END="32192"
    # offset at 0x01c0_0000 / 1024 = 28672
    FLASH_UBOOT2_OFFSET="28672"

    dd if=/dev/zero of=${DEPLOY_DIR_IMAGE}/image.bin bs=1k count=32768
    dd bs=1k conv=notrunc seek=$FLASH_UBOOT_OFFSET if=${DEPLOY_DIR_IMAGE}/u-boot-tmp.bin of=${DEPLOY_DIR_IMAGE}/image.bin
    dd bs=1k conv=notrunc seek=$FLASH_KERNEL_OFFSET if=${DEPLOY_DIR_IMAGE}/fitImage-proliant-image-proliant-yocto-proliant-yocto of=${DEPLOY_DIR_IMAGE}/image.bin
    dd bs=1k conv=notrunc seek=$FLASH_UBOOT2_OFFSET if=${DEPLOY_DIR_IMAGE}/u-boot-tmp.bin of=${DEPLOY_DIR_IMAGE}/image.bin
    dd bs=1k conv=notrunc seek=$FLASH_SECTION_OFFSET if=${DEPLOY_DIR_IMAGE}/hpe-section of=${DEPLOY_DIR_IMAGE}/image.bin
    dd bs=1k conv=notrunc seek=$FLASH_SECTION2_OFFSET if=${DEPLOY_DIR_IMAGE}/hpe-section2 of=${DEPLOY_DIR_IMAGE}/image.bin

    # Remove unnecessary files
    rm ${DEPLOY_DIR_IMAGE}/gxp-tmp.sig \
       ${DEPLOY_DIR_IMAGE}/gxp-uboot.sig \
       ${DEPLOY_DIR_IMAGE}/hpe-section \
       ${DEPLOY_DIR_IMAGE}/hpe-section2 \
       ${DEPLOY_DIR_IMAGE}/u-boot-tmp.bin
}

addtask generate_rom_image after do_image_complete before do_build