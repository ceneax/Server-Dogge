package ceneax.server.dogge.model.system;

import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HWDiskStore;
import oshi.hardware.NetworkIF;

public class DeviceInfoBean{

    private CentralProcessor cpu;
    private GlobalMemory memory;
    private HWDiskStore[] disk;
    private NetworkIF[] network;

    public CentralProcessor getCpu() {
        return cpu;
    }

    public void setCpu(CentralProcessor cpu) {
        this.cpu = cpu;
    }

    public GlobalMemory getMemory() {
        return memory;
    }

    public void setMemory(GlobalMemory memory) {
        this.memory = memory;
    }

    public HWDiskStore[] getDisk() {
        return disk;
    }

    public void setDisk(HWDiskStore[] disk) {
        this.disk = disk;
    }

    public NetworkIF[] getNetwork() {
        return network;
    }

    public void setNetwork(NetworkIF[] network) {
        this.network = network;
    }

}
