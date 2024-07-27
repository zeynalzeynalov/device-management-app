package org.abc.app.api;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(DeviceController.class)
public class DeviceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DeviceServiceImpl deviceService;

    @Test
    public void getApiStatus_whenCallingEndpoint_shouldReturnSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/devices/v1/status"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("status").value("success"))
                .andExpect(jsonPath("data").value("API is running."));
    }

    @Test
    public void getAllDevices_whenDeviceRepositoryEmpty_shouldReturnEmptyList() throws Exception {
        when(deviceService.getAll()).thenReturn(Collections.EMPTY_LIST);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/devices/v1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("status").value("success"))
                .andExpect(jsonPath("data").isEmpty());
    }
}
