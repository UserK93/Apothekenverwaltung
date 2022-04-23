package com.example.demo.Service;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.example.demo.Apotheke;
import com.example.demo.Controller.ApoController;
import com.example.demo.Repositories.ApoRepo;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ApoServiceTest {

    @Mock
    private ApoRepo apoRepoTest;
    @InjectMocks
    private ApoService servicetest;

    protected MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);// this is needed for inititalization of mocks, if you use @Mock
        ApoController controller = new ApoController(servicetest);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("if URL is right, check if given object parameters match with the expected Result")
    void getAllProductsTest() throws Exception {
        when(servicetest.getAllProducts())
                .thenReturn(List.of(new Apotheke(1, 2, "Aspirin", "Acetylsalicylsäure", "Bayer")));

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/getAllProducts"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].phNr").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].anzahl").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].produktname").value("Aspirin"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].wirkstoff").value("Acetylsalicylsäure"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].hersteller").value("Bayer"));

    }

    @Test
    @DisplayName("if produktname is present then compare to expected name given")
    void findProductByName() {

        Apotheke apothekeGiven = new Apotheke(1, 2, "Aspirin", "Acetylsalicylsäure", "Bayer");
        Apotheke apothekeExpected = new Apotheke(1, 2, "Aspirin", "Acetylsalicylsäure", "Bayer");

        when(apoRepoTest.findProductByName("Aspirin")).thenReturn(Optional.of(apothekeGiven));

        assertThat(apoRepoTest.findProductByName("Aspirin").get().getProduktname())
                .isEqualTo(apothekeExpected.getProduktname());

    }

    @Test
    @DisplayName("verify if new product is saved correctly to the repository")
    void addProduct() {

        Apotheke apothekeGiven = new Apotheke(1, 2, "Aspirin", "Acetylsalicylsäure", "Bayer");
        servicetest.addProduct(apothekeGiven);
        ArgumentCaptor<Apotheke> apothekeArgumentCaptor = ArgumentCaptor.forClass(Apotheke.class);

        verify(apoRepoTest).save(apothekeArgumentCaptor.capture());

        Apotheke apothekeCapture = apothekeArgumentCaptor.getValue();
        assertThat(apothekeCapture).isEqualTo(apothekeGiven);
    }

    @Test
    @DisplayName("checks if product could be deleted")
    void deleteProduct() throws Exception {

        Apotheke apothekeGiven = new Apotheke(1, 2, "Aspirin", "Acetylsalicylsäure", "Bayer");
        when(apoRepoTest.findById(apothekeGiven.getPhNr())).thenReturn(Optional.of(apothekeGiven));

        mockMvc.perform(MockMvcRequestBuilders.delete("/deleteProduct/{pharmazentralnummer}", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("checks if product could be edited")
    void editProduct() throws Exception {

        Apotheke apothekeGiven = new Apotheke(1, 2, "Aspirin", "Acetylsalicylsäure", "Bayer");

        when(apoRepoTest.findProductByName(apothekeGiven.getProduktname())).thenReturn(Optional.of(apothekeGiven));

        mockMvc.perform(MockMvcRequestBuilders.put("/edit/{produktname}", "Aspirin"));
        assertThat(apoRepoTest.findProductByName("Aspirin").get().getAnzahl()).isEqualTo(apothekeGiven.getAnzahl());

    }

    @Test
    @DisplayName("checks if amount could be decreased")
    void CheckIfOrderFromStoreWorks() throws Exception {

        Apotheke apothekeGiven = new Apotheke(1, 2, "Aspirin", "Acetylsalicylsäure", "Bayer");

        when(apoRepoTest.findProductByName(apothekeGiven.getProduktname())).thenReturn(Optional.of(apothekeGiven));

        mockMvc
                .perform(MockMvcRequestBuilders.put("/orderFromStore/{produktname},{anzahl}", "Aspirin", 2)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("checks if amount could be increases")
    void CheckIfOrderMoreWorks() throws Exception {

        Apotheke apothekeGiven = new Apotheke(1, 2, "Aspirin", "Acetylsalicylsäure", "Bayer");

        when(apoRepoTest.findProductByName(apothekeGiven.getProduktname())).thenReturn(Optional.of(apothekeGiven));
        mockMvc.perform(MockMvcRequestBuilders.put("/orderMore/{produktname},{anzahl}", "Aspirin", 2)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void checkIfOrderMoreIncreasesAmountCorrectly() throws Exception {
        int anzahl = 1;

        Apotheke apothekeGiven = new Apotheke(1, 2, "Aspirin", "Acetylsalicylsäure", "Bayer");
        Apotheke apothekeExpected = new Apotheke(1, apothekeGiven.getAnzahl() + anzahl, "Aspirin", "Acetylsalicylsäure",
                "Bayer");

        when(apoRepoTest.findProductByName(apothekeGiven.getProduktname())).thenReturn(Optional.of(apothekeGiven));

        mockMvc.perform(MockMvcRequestBuilders.put("/orderMore/{produktname},{anzahl}", "Aspirin", anzahl));

        assertThat(apoRepoTest.findProductByName("Aspirin").get().getAnzahl()).isEqualTo(apothekeExpected.getAnzahl());

    }

    @Test
    public void checkIfOrderFromStoreDecreasesAmountCorrectAndIsNotNegative() throws Exception {
        int anzahl = 1;
        Apotheke apothekeGiven = new Apotheke(1, 2, "Aspirin", "Acetylsalicylsäure", "Bayer");
        Apotheke apothekeExpected = new Apotheke(1, apothekeGiven.getAnzahl() - anzahl, "Aspirin", "Acetylsalicylsäure",
                "Bayer");
        when(apoRepoTest.findProductByName(apothekeGiven.getProduktname())).thenReturn(Optional.of(apothekeGiven));

        mockMvc.perform(MockMvcRequestBuilders.put("/orderFromStore/{produktname},{anzahl}", "Aspirin", anzahl));

        assertThat(apoRepoTest.findProductByName("Aspirin").get().getAnzahl()).isEqualTo(apothekeExpected.getAnzahl());

        assertThat(apothekeExpected.getAnzahl()).isNotNegative();
    }
}