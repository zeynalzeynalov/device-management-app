package org.abc.app.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.abc.app.common.RestResponse;
import org.abc.app.device.Device;
import org.abc.app.device.DeviceCreateRequest;
import org.abc.app.device.DeviceRepository;
import org.abc.app.device.DeviceUpdateRequest;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(DeviceController.class)
public class DeviceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DeviceServiceImpl deviceService;

    // Jackson object mapper for converting objects to JSON
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void getApiStatus_whenApiRunning_shouldReturnSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/devices/v1/status"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("status").value(RestResponse.SUCCESS))
                .andExpect(jsonPath("data").value("API is running."));
    }

    @Test
    public void getAllDevices_whenNotEmpty_shouldReturnDevices() throws Exception {
        Device device = Device.builder().id(1L).name("Test device name").brand("Test device brand").build();
        when(deviceService.getAll()).thenReturn(List.of(device));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/devices/v1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("status").value(RestResponse.SUCCESS))
                .andExpect(jsonPath("data[0].id").value(device.getId()))
                .andExpect(jsonPath("data[0].name").value(device.getName()))
                .andExpect(jsonPath("data[0].brand").value(device.getBrand()));
    }

    @Test
    public void getAllDevices_whenEmpty_shouldReturnEmptyList() throws Exception {
        when(deviceService.getAll()).thenReturn(List.of());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/devices/v1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("status").value(RestResponse.SUCCESS))
                .andExpect(jsonPath("data").isEmpty());
    }

    @Test
    public void getDeviceById_whenExists_shouldReturnDevice() throws Exception {
        Device device = Device.builder().id(1L).name("Test device name").brand("Test device brand").build();
        when(deviceService.getById(1)).thenReturn(device);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/devices/v1/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("status").value(RestResponse.SUCCESS))
                .andExpect(jsonPath("data.id").value(device.getId()))
                .andExpect(jsonPath("data.name").value(device.getName()))
                .andExpect(jsonPath("data.brand").value(device.getBrand()));
    }

    @Test
    public void getDeviceById_whenNotExists_shouldReturnFailure() throws Exception {
        when(deviceService.getById(666)).thenThrow(new DeviceRepository.DeviceNotFoundException(666));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/devices/v1/666"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("status").value(RestResponse.FAIL))
                .andExpect(jsonPath("data").value("Device with id=666 not found"));
    }

    @Test
    public void getAllDevicesFilteredByBrand_whenExists_shouldReturnFoundDevices() throws Exception {
        Device device = Device.builder().id(1L).name("Test device name").brand("Test device brand").build();
        when(deviceService.getAllFilteredByBrand("test")).thenReturn(List.of(device));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/devices/v1/filter?brand=test"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("status").value(RestResponse.SUCCESS))
                .andExpect(jsonPath("data[0].id").value(device.getId()))
                .andExpect(jsonPath("data[0].name").value(device.getName()))
                .andExpect(jsonPath("data[0].brand").value(device.getBrand()));
    }

    @Test
    public void getAllDevicesFilteredByBrand_whenNotExists_shouldReturnEmptyList() throws Exception {
        Device device = Device.builder().id(1L).name("Test device name").brand("Test device brand").build();
        when(deviceService.getAllFilteredByBrand("test")).thenReturn(List.of(device));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/devices/v1/filter?brand=test_new"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("status").value(RestResponse.SUCCESS))
                .andExpect(jsonPath("data").isEmpty());
    }

    @Test
    public void createDevice_whenSuccessful_shouldReturnSuccessAndDevice() throws Exception {
        DeviceCreateRequest createRequest = new DeviceCreateRequest();
        createRequest.setName("     Created device name      ");
        createRequest.setBrand("      Created device brand         ");

        Device device = Device.builder().id(1L).name(createRequest.name.trim()).brand(createRequest.brand.trim()).build();

        when(deviceService.create(createRequest)).thenReturn(device);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/devices/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("status").value(RestResponse.SUCCESS))
                .andExpect(jsonPath("data.id").value(device.getId()))
                .andExpect(jsonPath("data.name").value(createRequest.getName().trim()))
                .andExpect(jsonPath("data.brand").value(createRequest.getBrand().trim()));
    }

    @Test
    public void createDevice_whenMissingField_shouldReturnFailure() throws Exception {
        DeviceCreateRequest createRequest = new DeviceCreateRequest();
        createRequest.setName("Created device name");

        Device device = Device.builder().id(1L).name(createRequest.name).brand(createRequest.brand).build();

        when(deviceService.create(createRequest)).thenReturn(device);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/devices/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("status").value(RestResponse.FAIL))
                .andExpect(jsonPath("data").value("Device brand can not be blank"));
    }

    @Test
    public void updateDevice_whenSuccessful_shouldReturnSuccessAndDevice() throws Exception {
        DeviceUpdateRequest updateRequest = new DeviceUpdateRequest();
        updateRequest.setName("       Updated device name      ");
        updateRequest.setBrand("       Updated device brand       ");

        Device device = Device.builder()
                .id(1L)
                .name(updateRequest.getName().trim())
                .brand(updateRequest.getBrand().trim())
                .build();

        when(deviceService.update(1L, updateRequest)).thenReturn(device);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/devices/v1/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(RestResponse.SUCCESS))
                .andExpect(jsonPath("$.data.id").value(device.getId()))
                .andExpect(jsonPath("$.data.name").value(updateRequest.getName().trim()))
                .andExpect(jsonPath("$.data.brand").value(updateRequest.getBrand().trim()));
    }

    @Test
    public void updateDevice_whenPartially_shouldReturnSuccessAndDevice() throws Exception {
        DeviceUpdateRequest updateRequest = new DeviceUpdateRequest();
        updateRequest.setName("Updated device name");
        updateRequest.setBrand(null);

        Device device = Device.builder()
                .id(1L)
                .name(updateRequest.getName())
                .brand(updateRequest.getBrand())
                .build();

        when(deviceService.update(1L, updateRequest)).thenReturn(device);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/devices/v1/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(RestResponse.SUCCESS))
                .andExpect(jsonPath("$.data.id").value(device.getId()))
                .andExpect(jsonPath("$.data.name").value(device.getName()))
                .andExpect(jsonPath("$.data.brand").value(device.getBrand()));
    }

    @Test
    public void updateDevice_whenDeviceNotExists_shouldReturnFailure() throws Exception {
        DeviceUpdateRequest updateRequest = new DeviceUpdateRequest();
        updateRequest.setName("Updated device name");
        updateRequest.setBrand(null);

        when(deviceService.update(666, updateRequest)).thenThrow(new DeviceRepository.DeviceNotFoundException(666));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/devices/v1/666")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(RestResponse.FAIL))
                .andExpect(jsonPath("data").value("Device with id=666 not found"));
    }
}
