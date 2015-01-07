# Remove relative paths from $PATH
P=""
for p in $(echo $PATH | sed -e "s/:/\n/g");
do
    [[ ${p:0:1} == "/" ]] && P+=":"$p
done
export PATH=${P:1}
unset P

# Variables for building on BeagleBone Black
export U_BOOT_BIN_DIR=build/am335x_evm/
export CONSOLE=tty00
