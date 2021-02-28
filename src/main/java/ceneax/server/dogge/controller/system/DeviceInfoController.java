package ceneax.server.dogge.controller.system;

import ceneax.server.dogge.model.JsonResult;
import ceneax.server.dogge.model.system.DeviceInfoBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;

/**
 * Description: 控制器 获取设备信息
 * Author: ceneax
 * Website: ceneax.com
 * Date: 2021/2/28 19:31
 */
@RestController
@RequestMapping("/system")
public class DeviceInfoController {

    @GetMapping("/deviceInfo")
    public JsonResult<DeviceInfoBean> getDeviceInfo() {
        HardwareAbstractionLayer hardware = new SystemInfo().getHardware();
        DeviceInfoBean deviceInfoBean = new DeviceInfoBean();
        deviceInfoBean.setCpu(hardware.getProcessor());
        deviceInfoBean.setMemory(hardware.getMemory());
        deviceInfoBean.setDisk(hardware.getDiskStores());
//        deviceInfoBean.setNetwork(hardware.getNetworkIFs());
        return new JsonResult<>(deviceInfoBean);
    }

}
