package com.bekrenovr.spotkajmysie;

import com.bekrenovr.spotkajmysie.controller.TimeRangeController;
import com.bekrenovr.spotkajmysie.service.TimeRangeService;
import org.json.JSONArray;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import java.io.IOException;
import java.io.InputStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TimeRangeController.class)
public class TimeRangeControllerTests {
    private static final String REQUEST_BODY_VALID = "/test/request-body-valid.json";

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TimeRangeService timeRangeService;

    @Test
    public void testGenerateTimeRanges_Basic() throws Exception {
        String meetingDuration = "00:30";
        RequestBuilder request = post("/generate-time-ranges?meeting_duration={duration}", meetingDuration)
                .contentType(MediaType.APPLICATION_JSON)
                .content(loadRequestBody(REQUEST_BODY_VALID));
        mockMvc.perform(request)
                .andExpect(status().isOk());
    }

    @Test
    public void testGenerateTimeRanges_ShouldReturn400_WhenLessThan2CalendarsPresent() throws Exception {
        JSONArray jsonArray = new JSONArray(loadRequestBody(REQUEST_BODY_VALID));
        String requestBodyInvalid = jsonArray.getJSONObject(0).toString();
        String meetingDuration = "00:30";

        RequestBuilder request = post("/generate-time-ranges?meeting_duration={duration}", meetingDuration)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBodyInvalid);
        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGenerateTimeRanges_ShouldReturn400_WhenMeetingDurationIsInvalid() throws Exception {
        String meetingDuration = "asd";
        when(timeRangeService.generateTimeRanges(any(), any()))
                .thenThrow(NumberFormatException.class);

        RequestBuilder request = post("/generate-time-ranges?meeting_duration={duration}", meetingDuration)
                .contentType(MediaType.APPLICATION_JSON)
                .content(loadRequestBody(REQUEST_BODY_VALID));
        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    private String loadRequestBody(String path){
        try(InputStream is = getClass().getResourceAsStream(path)){
            return new String(is.readAllBytes());
        } catch(IOException ex){
            throw new RuntimeException(ex);
        }
    }
}
