package org.abc.app.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.inject.Inject;

import org.abc.app.device.Device;
import org.abc.app.dto.DeviceCreateRequest;
import org.abc.app.repository.DeviceRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class DeviceServiceImplTest {

    Device device =
            Device.builder()
                    .id(null)
                    .createdAt(null)
                    .updatedAt(null)
                    .name("Test device name")
                    .brand("Test device brand")
                    .build();
    @Mock private DeviceRepository deviceRepository;

    @InjectMocks @Inject private DeviceServiceImpl deviceService;

    @Test
    public void getAllDevices_whenValidRequest_shouldReturnSuccessfully() {
        when(deviceRepository.findAll()).thenReturn(List.of(device));

        List<Device> deviceList = deviceService.getAll();

        assertThat(deviceList).isNotNull();
        assertThat(deviceList).isEqualTo(List.of(device));
        assertThat(deviceList.get(0).getName()).isEqualTo(device.getName());
        assertThat(deviceList.get(0).getBrand()).isEqualTo(device.getBrand());
    }

    @Test
    public void getAllDevices_whenRepositoryIdEmpty_shouldReturnEmptyListSuccessfully() {
        when(deviceRepository.findAll()).thenReturn(List.of());

        List<Device> deviceList = deviceService.getAll();

        assertThat(deviceList).isNotNull();
        assertThat(deviceList).isEmpty();
    }

    @Test
    public void getAllDevices_whenMatch_shouldReturnListSuccessfully() {
        when(deviceRepository.findByBrandContaining("test")).thenReturn(List.of(device));

        List<Device> deviceList = deviceService.getAllFilteredByBrand("test");

        assertThat(deviceList).isNotNull();
        assertThat(deviceList).isEqualTo(List.of(device));
        assertThat(deviceList.get(0).getName()).isEqualTo(device.getName());
        assertThat(deviceList.get(0).getBrand()).isEqualTo(device.getBrand());
    }

    @Test
    public void getAllDevices_whenFilterNotMatch_shouldReturnEmptyListSuccessfully() {
        when(deviceRepository.findByBrandContaining("test")).thenReturn(List.of());

        List<Device> deviceList = deviceService.getAllFilteredByBrand("test");

        assertThat(deviceList).isNotNull();
        assertThat(deviceList).isEmpty();
    }

    @Test
    public void getDeviceById_whenFound_shouldReturnSuccessfully() {
        when(deviceRepository.existsById(666L)).thenReturn(true);
        when(deviceRepository.findById(666L)).thenReturn(Optional.ofNullable(device));

        Device deviceFound = deviceService.getById(666L);

        assertThat(deviceFound).isNotNull();
        assertThat(deviceFound).isEqualTo(device);
        assertThat(deviceFound.getName()).isEqualTo(device.getName());
        assertThat(deviceFound.getBrand()).isEqualTo(device.getBrand());
    }

    @Test
    public void getDeviceById_whenNotFound_shouldReturnFailSuccessfully() {
        when(deviceRepository.existsById(666L)).thenReturn(false);

        Exception exception =
                assertThrows(
                        DeviceNotFoundException.class,
                        () -> {
                            Device deviceFound = deviceService.getById(666L);
                        });

        assertTrue(exception.getMessage().contains("Device with id=666 not found"));
    }

    @Test
    public void createDevice_whenValidRequest_shouldReturnSuccessfully() {
        DeviceCreateRequest createRequest =
                DeviceCreateRequest.builder()
                        .name(device.getName())
                        .brand(device.getBrand())
                        .build();

        when(deviceRepository.save(device)).thenReturn(device);

        Device savedDevice = deviceService.create(createRequest);

        assertThat(savedDevice).isEqualTo(device);
        assertThat(savedDevice).isNotNull();
        assertThat(savedDevice.getName()).isEqualTo(createRequest.getName());
        assertThat(savedDevice.getBrand()).isEqualTo(createRequest.getBrand());
    }

    @Test
    public void createDevice_whenFieldsNeedTrim_shouldReturnTrimmedFieldsSuccessfully() {
        DeviceCreateRequest createRequest =
                DeviceCreateRequest.builder()
                        .name("                    " + device.getName() + "     ")
                        .brand("    " + device.getBrand() + "         ")
                        .build();

        when(deviceRepository.save(device)).thenReturn(device);

        Device savedDevice = deviceService.create(createRequest);

        assertThat(savedDevice).isEqualTo(device);
        assertThat(savedDevice).isNotNull();
        assertThat(savedDevice.getName()).isEqualTo(createRequest.getName().trim());
        assertThat(savedDevice.getBrand()).isEqualTo(createRequest.getBrand().trim());
    }

    @Test
    public void updateDevice_whenValidRequest_shouldReturnSuccessfully()
            throws JsonMappingException {
        DeviceUpdateRequest updateRequest =
                DeviceUpdateRequest.builder()
                        .name("           Updated device name     ")
                        .brand("    Updated device brand         ")
                        .build();

        deviceService.setObjectMapper(new ObjectMapper());

        when(deviceRepository.existsById(666L)).thenReturn(true);
        when(deviceRepository.findById(666L)).thenReturn(Optional.ofNullable(device));
        when(deviceRepository.save(device)).thenReturn(device);

        Device savedDevice = deviceService.update(666L, updateRequest);

        assertThat(savedDevice).isEqualTo(device);
        assertThat(savedDevice).isNotNull();
        assertThat(savedDevice.getName()).isEqualTo(updateRequest.getName().trim());
        assertThat(savedDevice.getBrand()).isEqualTo(updateRequest.getBrand().trim());
    }

    @Test
    public void updateDevice_whenNotExists_shouldReturnFailSuccessfully()
            throws JsonMappingException {
        when(deviceRepository.existsById(666L)).thenReturn(false);

        Exception exception =
                assertThrows(
                        DeviceNotFoundException.class,
                        () -> {
                            Device deviceFound =
                                    deviceService.update(
                                            666L, DeviceUpdateRequest.builder().build());
                        });

        assertTrue(exception.getMessage().contains("Device with id=666 not found"));
    }

    @Test
    public void deleteDevice_whenValidRequest_shouldReturnSuccessfully()
            throws JsonMappingException {
        when(deviceRepository.existsById(666L)).thenReturn(true);
        when(deviceRepository.findById(666L)).thenReturn(Optional.ofNullable(device));

        Boolean result = deviceService.delete(666L);

        assertThat(result).isTrue();
    }

    @Test
    public void deleteDevice_whenNotExists_shouldReturnFailSuccessfully()
            throws JsonMappingException {
        when(deviceRepository.existsById(666L)).thenReturn(false);

        Exception exception =
                assertThrows(
                        DeviceNotFoundException.class,
                        () -> {
                            boolean result = deviceService.delete(666L);
                        });

        assertTrue(exception.getMessage().contains("Device with id=666 not found"));
    }
}
