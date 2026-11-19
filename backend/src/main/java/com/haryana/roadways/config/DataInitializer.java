package com.haryana.roadways.config;

import com.haryana.roadways.entity.*;
import com.haryana.roadways.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Component
public class DataInitializer implements CommandLineRunner {

    private final BusStopRepository busStopRepository;
    private final RouteRepository routeRepository;
    private final BusRepository busRepository;
    private final TripRepository tripRepository;
    private final BusScheduleRepository busScheduleRepository;

    public DataInitializer(
            BusStopRepository busStopRepository,
            RouteRepository routeRepository,
            BusRepository busRepository,
            TripRepository tripRepository,
            BusScheduleRepository busScheduleRepository) {
        this.busStopRepository = busStopRepository;
        this.routeRepository = routeRepository;
        this.busRepository = busRepository;
        this.tripRepository = tripRepository;
        this.busScheduleRepository = busScheduleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (busStopRepository.count() > 0) {
            return; // Already initialized
        }

        System.out.println("Initializing Haryana Roadways comprehensive network for all 22 districts...");

        Map<String, BusStop> stopMap = new HashMap<>();

        BusStop s_stop_amb_cantt = new BusStop();
        s_stop_amb_cantt.setName("Ambala Cantt General Bus Stand");
        s_stop_amb_cantt.setDistrict("Ambala");
        s_stop_amb_cantt.setLatitude(30.342);
        s_stop_amb_cantt.setLongitude(76.84);
        s_stop_amb_cantt = busStopRepository.save(s_stop_amb_cantt);
        stopMap.put("stop-amb-cantt", s_stop_amb_cantt);

        BusStop s_stop_amb_city = new BusStop();
        s_stop_amb_city.setName("Ambala City Bus Stand");
        s_stop_amb_city.setDistrict("Ambala");
        s_stop_amb_city.setLatitude(30.378);
        s_stop_amb_city.setLongitude(76.776);
        s_stop_amb_city = busStopRepository.save(s_stop_amb_city);
        stopMap.put("stop-amb-city", s_stop_amb_city);

        BusStop s_stop_amb_naraingarh = new BusStop();
        s_stop_amb_naraingarh.setName("Naraingarh Bus Stand");
        s_stop_amb_naraingarh.setDistrict("Ambala");
        s_stop_amb_naraingarh.setLatitude(30.481);
        s_stop_amb_naraingarh.setLongitude(77.132);
        s_stop_amb_naraingarh = busStopRepository.save(s_stop_amb_naraingarh);
        stopMap.put("stop-amb-naraingarh", s_stop_amb_naraingarh);

        BusStop s_stop_bhw_stand = new BusStop();
        s_stop_bhw_stand.setName("Bhiwani General Bus Stand");
        s_stop_bhw_stand.setDistrict("Bhiwani");
        s_stop_bhw_stand.setLatitude(28.7885);
        s_stop_bhw_stand.setLongitude(76.1365);
        s_stop_bhw_stand = busStopRepository.save(s_stop_bhw_stand);
        stopMap.put("stop-bhw-stand", s_stop_bhw_stand);

        BusStop s_stop_bhw_tosham = new BusStop();
        s_stop_bhw_tosham.setName("Tosham Bus Stand");
        s_stop_bhw_tosham.setDistrict("Bhiwani");
        s_stop_bhw_tosham.setLatitude(28.874);
        s_stop_bhw_tosham.setLongitude(75.918);
        s_stop_bhw_tosham = busStopRepository.save(s_stop_bhw_tosham);
        stopMap.put("stop-bhw-tosham", s_stop_bhw_tosham);

        BusStop s_stop_bhw_loharu = new BusStop();
        s_stop_bhw_loharu.setName("Loharu Bus Stand");
        s_stop_bhw_loharu.setDistrict("Bhiwani");
        s_stop_bhw_loharu.setLatitude(28.423);
        s_stop_bhw_loharu.setLongitude(75.812);
        s_stop_bhw_loharu = busStopRepository.save(s_stop_bhw_loharu);
        stopMap.put("stop-bhw-loharu", s_stop_bhw_loharu);

        BusStop s_stop_kitlana = new BusStop();
        s_stop_kitlana.setName("Kitlana Toll & Crossing");
        s_stop_kitlana.setDistrict("Bhiwani");
        s_stop_kitlana.setLatitude(28.685);
        s_stop_kitlana.setLongitude(76.23);
        s_stop_kitlana = busStopRepository.save(s_stop_kitlana);
        stopMap.put("stop-kitlana", s_stop_kitlana);

        BusStop s_stop_cd_bus_stand = new BusStop();
        s_stop_cd_bus_stand.setName("Charkhi Dadri Main Bus Stand");
        s_stop_cd_bus_stand.setDistrict("Charkhi Dadri");
        s_stop_cd_bus_stand.setLatitude(28.5921);
        s_stop_cd_bus_stand.setLongitude(76.2657);
        s_stop_cd_bus_stand = busStopRepository.save(s_stop_cd_bus_stand);
        stopMap.put("stop-cd-bus-stand", s_stop_cd_bus_stand);

        BusStop s_stop_cd_loharu_chowk = new BusStop();
        s_stop_cd_loharu_chowk.setName("Loharu Chowk, Dadri");
        s_stop_cd_loharu_chowk.setDistrict("Charkhi Dadri");
        s_stop_cd_loharu_chowk.setLatitude(28.5938);
        s_stop_cd_loharu_chowk.setLongitude(76.2612);
        s_stop_cd_loharu_chowk = busStopRepository.save(s_stop_cd_loharu_chowk);
        stopMap.put("stop-cd-loharu-chowk", s_stop_cd_loharu_chowk);

        BusStop s_stop_cd_bhiwani_chungi = new BusStop();
        s_stop_cd_bhiwani_chungi.setName("Bhiwani Chungi, Dadri");
        s_stop_cd_bhiwani_chungi.setDistrict("Charkhi Dadri");
        s_stop_cd_bhiwani_chungi.setLatitude(28.608);
        s_stop_cd_bhiwani_chungi.setLongitude(76.261);
        s_stop_cd_bhiwani_chungi = busStopRepository.save(s_stop_cd_bhiwani_chungi);
        stopMap.put("stop-cd-bhiwani-chungi", s_stop_cd_bhiwani_chungi);

        BusStop s_stop_cd_badhra = new BusStop();
        s_stop_cd_badhra.setName("Badhra Bus Stand");
        s_stop_cd_badhra.setDistrict("Charkhi Dadri");
        s_stop_cd_badhra.setLatitude(28.502);
        s_stop_cd_badhra.setLongitude(76.082);
        s_stop_cd_badhra = busStopRepository.save(s_stop_cd_badhra);
        stopMap.put("stop-cd-badhra", s_stop_cd_badhra);

        BusStop s_stop_fbd_nit = new BusStop();
        s_stop_fbd_nit.setName("Faridabad NIT Bus Stand");
        s_stop_fbd_nit.setDistrict("Faridabad");
        s_stop_fbd_nit.setLatitude(28.388);
        s_stop_fbd_nit.setLongitude(77.305);
        s_stop_fbd_nit = busStopRepository.save(s_stop_fbd_nit);
        stopMap.put("stop-fbd-nit", s_stop_fbd_nit);

        BusStop s_stop_ballabgarh = new BusStop();
        s_stop_ballabgarh.setName("Ballabgarh Raja Nahar Singh Terminal");
        s_stop_ballabgarh.setDistrict("Faridabad");
        s_stop_ballabgarh.setLatitude(28.3385);
        s_stop_ballabgarh.setLongitude(77.3245);
        s_stop_ballabgarh = busStopRepository.save(s_stop_ballabgarh);
        stopMap.put("stop-ballabgarh", s_stop_ballabgarh);

        BusStop s_stop_fbd_badarpur = new BusStop();
        s_stop_fbd_badarpur.setName("Badarpur Border Terminal");
        s_stop_fbd_badarpur.setDistrict("Faridabad");
        s_stop_fbd_badarpur.setLatitude(28.495);
        s_stop_fbd_badarpur.setLongitude(77.301);
        s_stop_fbd_badarpur = busStopRepository.save(s_stop_fbd_badarpur);
        stopMap.put("stop-fbd-badarpur", s_stop_fbd_badarpur);

        BusStop s_stop_fat_stand = new BusStop();
        s_stop_fat_stand.setName("Fatehabad Bus Stand");
        s_stop_fat_stand.setDistrict("Fatehabad");
        s_stop_fat_stand.setLatitude(29.512);
        s_stop_fat_stand.setLongitude(75.452);
        s_stop_fat_stand = busStopRepository.save(s_stop_fat_stand);
        stopMap.put("stop-fat-stand", s_stop_fat_stand);

        BusStop s_stop_fat_tohana = new BusStop();
        s_stop_fat_tohana.setName("Tohana Bus Stand");
        s_stop_fat_tohana.setDistrict("Fatehabad");
        s_stop_fat_tohana.setLatitude(29.7042);
        s_stop_fat_tohana.setLongitude(75.9015);
        s_stop_fat_tohana = busStopRepository.save(s_stop_fat_tohana);
        stopMap.put("stop-fat-tohana", s_stop_fat_tohana);

        BusStop s_stop_fat_ratia = new BusStop();
        s_stop_fat_ratia.setName("Ratia Bus Stand");
        s_stop_fat_ratia.setDistrict("Fatehabad");
        s_stop_fat_ratia.setLatitude(29.682);
        s_stop_fat_ratia.setLongitude(75.578);
        s_stop_fat_ratia = busStopRepository.save(s_stop_fat_ratia);
        stopMap.put("stop-fat-ratia", s_stop_fat_ratia);

        BusStop s_stop_fat_bhuna = new BusStop();
        s_stop_fat_bhuna.setName("Bhuna Bus Stop");
        s_stop_fat_bhuna.setDistrict("Fatehabad");
        s_stop_fat_bhuna.setLatitude(29.531);
        s_stop_fat_bhuna.setLongitude(75.712);
        s_stop_fat_bhuna = busStopRepository.save(s_stop_fat_bhuna);
        stopMap.put("stop-fat-bhuna", s_stop_fat_bhuna);

        BusStop s_stop_ggn_stand = new BusStop();
        s_stop_ggn_stand.setName("Gurugram General Bus Stand");
        s_stop_ggn_stand.setDistrict("Gurugram");
        s_stop_ggn_stand.setLatitude(28.461);
        s_stop_ggn_stand.setLongitude(77.025);
        s_stop_ggn_stand = busStopRepository.save(s_stop_ggn_stand);
        stopMap.put("stop-ggn-stand", s_stop_ggn_stand);

        BusStop s_stop_ggn_iffco = new BusStop();
        s_stop_ggn_iffco.setName("IFFCO Chowk, Gurugram");
        s_stop_ggn_iffco.setDistrict("Gurugram");
        s_stop_ggn_iffco.setLatitude(28.472);
        s_stop_ggn_iffco.setLongitude(77.0725);
        s_stop_ggn_iffco = busStopRepository.save(s_stop_ggn_iffco);
        stopMap.put("stop-ggn-iffco", s_stop_ggn_iffco);

        BusStop s_stop_ggn_rajiv = new BusStop();
        s_stop_ggn_rajiv.setName("Rajiv Chowk, Gurugram");
        s_stop_ggn_rajiv.setDistrict("Gurugram");
        s_stop_ggn_rajiv.setLatitude(28.448);
        s_stop_ggn_rajiv.setLongitude(77.035);
        s_stop_ggn_rajiv = busStopRepository.save(s_stop_ggn_rajiv);
        stopMap.put("stop-ggn-rajiv", s_stop_ggn_rajiv);

        BusStop s_stop_ggn_manesar = new BusStop();
        s_stop_ggn_manesar.setName("Manesar IMT Chowk");
        s_stop_ggn_manesar.setDistrict("Gurugram");
        s_stop_ggn_manesar.setLatitude(28.355);
        s_stop_ggn_manesar.setLongitude(76.94);
        s_stop_ggn_manesar = busStopRepository.save(s_stop_ggn_manesar);
        stopMap.put("stop-ggn-manesar", s_stop_ggn_manesar);

        BusStop s_stop_ggn_sohna = new BusStop();
        s_stop_ggn_sohna.setName("Sohna Bus Stand");
        s_stop_ggn_sohna.setDistrict("Gurugram");
        s_stop_ggn_sohna.setLatitude(28.248);
        s_stop_ggn_sohna.setLongitude(77.062);
        s_stop_ggn_sohna = busStopRepository.save(s_stop_ggn_sohna);
        stopMap.put("stop-ggn-sohna", s_stop_ggn_sohna);

        BusStop s_stop_ggn_pataudi = new BusStop();
        s_stop_ggn_pataudi.setName("Pataudi Bus Stand");
        s_stop_ggn_pataudi.setDistrict("Gurugram");
        s_stop_ggn_pataudi.setLatitude(28.324);
        s_stop_ggn_pataudi.setLongitude(76.778);
        s_stop_ggn_pataudi = busStopRepository.save(s_stop_ggn_pataudi);
        stopMap.put("stop-ggn-pataudi", s_stop_ggn_pataudi);

        BusStop s_stop_hsr_stand = new BusStop();
        s_stop_hsr_stand.setName("Hisar New Bus Stand");
        s_stop_hsr_stand.setDistrict("Hisar");
        s_stop_hsr_stand.setLatitude(29.1539);
        s_stop_hsr_stand.setLongitude(75.7229);
        s_stop_hsr_stand = busStopRepository.save(s_stop_hsr_stand);
        stopMap.put("stop-hsr-stand", s_stop_hsr_stand);

        BusStop s_stop_hansi = new BusStop();
        s_stop_hansi.setName("Hansi Bus Stand");
        s_stop_hansi.setDistrict("Hisar");
        s_stop_hansi.setLatitude(29.102);
        s_stop_hansi.setLongitude(75.962);
        s_stop_hansi = busStopRepository.save(s_stop_hansi);
        stopMap.put("stop-hansi", s_stop_hansi);

        BusStop s_stop_hsr_barwala = new BusStop();
        s_stop_hsr_barwala.setName("Barwala Bus Stand");
        s_stop_hsr_barwala.setDistrict("Hisar");
        s_stop_hsr_barwala.setLatitude(29.378);
        s_stop_hsr_barwala.setLongitude(75.912);
        s_stop_hsr_barwala = busStopRepository.save(s_stop_hsr_barwala);
        stopMap.put("stop-hsr-barwala", s_stop_hsr_barwala);

        BusStop s_stop_hsr_agroha = new BusStop();
        s_stop_hsr_agroha.setName("Agroha Dham Chowk");
        s_stop_hsr_agroha.setDistrict("Hisar");
        s_stop_hsr_agroha.setLatitude(29.328);
        s_stop_hsr_agroha.setLongitude(75.615);
        s_stop_hsr_agroha = busStopRepository.save(s_stop_hsr_agroha);
        stopMap.put("stop-hsr-agroha", s_stop_hsr_agroha);

        BusStop s_stop_jhj_stand = new BusStop();
        s_stop_jhj_stand.setName("Jhajjar Main Bus Stand");
        s_stop_jhj_stand.setDistrict("Jhajjar");
        s_stop_jhj_stand.setLatitude(28.608);
        s_stop_jhj_stand.setLongitude(76.656);
        s_stop_jhj_stand = busStopRepository.save(s_stop_jhj_stand);
        stopMap.put("stop-jhj-stand", s_stop_jhj_stand);

        BusStop s_stop_bahadurgarh = new BusStop();
        s_stop_bahadurgarh.setName("Bahadurgarh Bus Stand");
        s_stop_bahadurgarh.setDistrict("Jhajjar");
        s_stop_bahadurgarh.setLatitude(28.692);
        s_stop_bahadurgarh.setLongitude(76.924);
        s_stop_bahadurgarh = busStopRepository.save(s_stop_bahadurgarh);
        stopMap.put("stop-bahadurgarh", s_stop_bahadurgarh);

        BusStop s_stop_jhj_beri = new BusStop();
        s_stop_jhj_beri.setName("Beri Town Bus Stop");
        s_stop_jhj_beri.setDistrict("Jhajjar");
        s_stop_jhj_beri.setLatitude(28.705);
        s_stop_jhj_beri.setLongitude(76.582);
        s_stop_jhj_beri = busStopRepository.save(s_stop_jhj_beri);
        stopMap.put("stop-jhj-beri", s_stop_jhj_beri);

        BusStop s_stop_jhj_badli = new BusStop();
        s_stop_jhj_badli.setName("Badli Chowk");
        s_stop_jhj_badli.setDistrict("Jhajjar");
        s_stop_jhj_badli.setLatitude(28.583);
        s_stop_jhj_badli.setLongitude(76.812);
        s_stop_jhj_badli = busStopRepository.save(s_stop_jhj_badli);
        stopMap.put("stop-jhj-badli", s_stop_jhj_badli);

        BusStop s_stop_jnd_stand = new BusStop();
        s_stop_jnd_stand.setName("Jind General Bus Stand");
        s_stop_jnd_stand.setDistrict("Jind");
        s_stop_jnd_stand.setLatitude(29.316);
        s_stop_jnd_stand.setLongitude(76.315);
        s_stop_jnd_stand = busStopRepository.save(s_stop_jnd_stand);
        stopMap.put("stop-jnd-stand", s_stop_jnd_stand);

        BusStop s_stop_jnd_narwana = new BusStop();
        s_stop_jnd_narwana.setName("Narwana Bus Stand");
        s_stop_jnd_narwana.setDistrict("Jind");
        s_stop_jnd_narwana.setLatitude(29.605);
        s_stop_jnd_narwana.setLongitude(76.115);
        s_stop_jnd_narwana = busStopRepository.save(s_stop_jnd_narwana);
        stopMap.put("stop-jnd-narwana", s_stop_jnd_narwana);

        BusStop s_stop_jnd_safidon = new BusStop();
        s_stop_jnd_safidon.setName("Safidon Bus Stand");
        s_stop_jnd_safidon.setDistrict("Jind");
        s_stop_jnd_safidon.setLatitude(29.412);
        s_stop_jnd_safidon.setLongitude(76.668);
        s_stop_jnd_safidon = busStopRepository.save(s_stop_jnd_safidon);
        stopMap.put("stop-jnd-safidon", s_stop_jnd_safidon);

        BusStop s_stop_jnd_julana = new BusStop();
        s_stop_jnd_julana.setName("Julana Bus Stop");
        s_stop_jnd_julana.setDistrict("Jind");
        s_stop_jnd_julana.setLatitude(29.122);
        s_stop_jnd_julana.setLongitude(76.395);
        s_stop_jnd_julana = busStopRepository.save(s_stop_jnd_julana);
        stopMap.put("stop-jnd-julana", s_stop_jnd_julana);

        BusStop s_stop_kth_stand = new BusStop();
        s_stop_kth_stand.setName("Kaithal New Bus Stand");
        s_stop_kth_stand.setDistrict("Kaithal");
        s_stop_kth_stand.setLatitude(29.8015);
        s_stop_kth_stand.setLongitude(76.401);
        s_stop_kth_stand = busStopRepository.save(s_stop_kth_stand);
        stopMap.put("stop-kth-stand", s_stop_kth_stand);

        BusStop s_stop_kth_cheeka = new BusStop();
        s_stop_kth_cheeka.setName("Guhla Cheeka Bus Stand");
        s_stop_kth_cheeka.setDistrict("Kaithal");
        s_stop_kth_cheeka.setLatitude(30.038);
        s_stop_kth_cheeka.setLongitude(76.302);
        s_stop_kth_cheeka = busStopRepository.save(s_stop_kth_cheeka);
        stopMap.put("stop-kth-cheeka", s_stop_kth_cheeka);

        BusStop s_stop_kth_kalayat = new BusStop();
        s_stop_kth_kalayat.setName("Kalayat Bus Stand");
        s_stop_kth_kalayat.setDistrict("Kaithal");
        s_stop_kth_kalayat.setLatitude(29.675);
        s_stop_kth_kalayat.setLongitude(76.255);
        s_stop_kth_kalayat = busStopRepository.save(s_stop_kth_kalayat);
        stopMap.put("stop-kth-kalayat", s_stop_kth_kalayat);

        BusStop s_stop_kth_pundri = new BusStop();
        s_stop_kth_pundri.setName("Pundri Bus Stop");
        s_stop_kth_pundri.setDistrict("Kaithal");
        s_stop_kth_pundri.setLatitude(29.758);
        s_stop_kth_pundri.setLongitude(76.561);
        s_stop_kth_pundri = busStopRepository.save(s_stop_kth_pundri);
        stopMap.put("stop-kth-pundri", s_stop_kth_pundri);

        BusStop s_stop_krl_stand = new BusStop();
        s_stop_krl_stand.setName("Karnal New Bus Stand");
        s_stop_krl_stand.setDistrict("Karnal");
        s_stop_krl_stand.setLatitude(29.6857);
        s_stop_krl_stand.setLongitude(76.9905);
        s_stop_krl_stand = busStopRepository.save(s_stop_krl_stand);
        stopMap.put("stop-krl-stand", s_stop_krl_stand);

        BusStop s_stop_krl_nirmal = new BusStop();
        s_stop_krl_nirmal.setName("Nirmal Kutiya, Karnal");
        s_stop_krl_nirmal.setDistrict("Karnal");
        s_stop_krl_nirmal.setLatitude(29.691);
        s_stop_krl_nirmal.setLongitude(76.985);
        s_stop_krl_nirmal = busStopRepository.save(s_stop_krl_nirmal);
        stopMap.put("stop-krl-nirmal", s_stop_krl_nirmal);

        BusStop s_stop_gharaunda = new BusStop();
        s_stop_gharaunda.setName("Gharaunda Stop");
        s_stop_gharaunda.setDistrict("Karnal");
        s_stop_gharaunda.setLatitude(29.54);
        s_stop_gharaunda.setLongitude(76.97);
        s_stop_gharaunda = busStopRepository.save(s_stop_gharaunda);
        stopMap.put("stop-gharaunda", s_stop_gharaunda);

        BusStop s_stop_krl_assandh = new BusStop();
        s_stop_krl_assandh.setName("Assandh Bus Stand");
        s_stop_krl_assandh.setDistrict("Karnal");
        s_stop_krl_assandh.setLatitude(29.521);
        s_stop_krl_assandh.setLongitude(76.602);
        s_stop_krl_assandh = busStopRepository.save(s_stop_krl_assandh);
        stopMap.put("stop-krl-assandh", s_stop_krl_assandh);

        BusStop s_stop_krl_taraori = new BusStop();
        s_stop_krl_taraori.setName("Taraori Stop");
        s_stop_krl_taraori.setDistrict("Karnal");
        s_stop_krl_taraori.setLatitude(29.805);
        s_stop_krl_taraori.setLongitude(76.928);
        s_stop_krl_taraori = busStopRepository.save(s_stop_krl_taraori);
        stopMap.put("stop-krl-taraori", s_stop_krl_taraori);

        BusStop s_stop_krk_pipli = new BusStop();
        s_stop_krk_pipli.setName("Pipli Bus Stand (Kurukshetra)");
        s_stop_krk_pipli.setDistrict("Kurukshetra");
        s_stop_krk_pipli.setLatitude(29.972);
        s_stop_krk_pipli.setLongitude(76.885);
        s_stop_krk_pipli = busStopRepository.save(s_stop_krk_pipli);
        stopMap.put("stop-krk-pipli", s_stop_krk_pipli);

        BusStop s_stop_krk_stand = new BusStop();
        s_stop_krk_stand.setName("Kurukshetra Thanesar Bus Stand");
        s_stop_krk_stand.setDistrict("Kurukshetra");
        s_stop_krk_stand.setLatitude(29.969);
        s_stop_krk_stand.setLongitude(76.828);
        s_stop_krk_stand = busStopRepository.save(s_stop_krk_stand);
        stopMap.put("stop-krk-stand", s_stop_krk_stand);

        BusStop s_stop_krk_uni = new BusStop();
        s_stop_krk_uni.setName("Kurukshetra University 3rd Gate");
        s_stop_krk_uni.setDistrict("Kurukshetra");
        s_stop_krk_uni.setLatitude(29.965);
        s_stop_krk_uni.setLongitude(76.818);
        s_stop_krk_uni = busStopRepository.save(s_stop_krk_uni);
        stopMap.put("stop-krk-uni", s_stop_krk_uni);

        BusStop s_stop_krk_shahbad = new BusStop();
        s_stop_krk_shahbad.setName("Shahbad Markanda Bus Stand");
        s_stop_krk_shahbad.setDistrict("Kurukshetra");
        s_stop_krk_shahbad.setLatitude(30.168);
        s_stop_krk_shahbad.setLongitude(76.872);
        s_stop_krk_shahbad = busStopRepository.save(s_stop_krk_shahbad);
        stopMap.put("stop-krk-shahbad", s_stop_krk_shahbad);

        BusStop s_stop_krk_pehowa = new BusStop();
        s_stop_krk_pehowa.setName("Pehowa Bus Stand");
        s_stop_krk_pehowa.setDistrict("Kurukshetra");
        s_stop_krk_pehowa.setLatitude(29.981);
        s_stop_krk_pehowa.setLongitude(76.582);
        s_stop_krk_pehowa = busStopRepository.save(s_stop_krk_pehowa);
        stopMap.put("stop-krk-pehowa", s_stop_krk_pehowa);

        BusStop s_stop_mnd_narnaul = new BusStop();
        s_stop_mnd_narnaul.setName("Narnaul General Bus Stand");
        s_stop_mnd_narnaul.setDistrict("Mahendragarh");
        s_stop_mnd_narnaul.setLatitude(28.0435);
        s_stop_mnd_narnaul.setLongitude(76.107);
        s_stop_mnd_narnaul = busStopRepository.save(s_stop_mnd_narnaul);
        stopMap.put("stop-mnd-narnaul", s_stop_mnd_narnaul);

        BusStop s_stop_mnd_stand = new BusStop();
        s_stop_mnd_stand.setName("Mahendragarh Main Bus Stand");
        s_stop_mnd_stand.setDistrict("Mahendragarh");
        s_stop_mnd_stand.setLatitude(28.275);
        s_stop_mnd_stand.setLongitude(76.152);
        s_stop_mnd_stand = busStopRepository.save(s_stop_mnd_stand);
        stopMap.put("stop-mnd-stand", s_stop_mnd_stand);

        BusStop s_stop_mnd_ateli = new BusStop();
        s_stop_mnd_ateli.setName("Ateli Mandi Chowk");
        s_stop_mnd_ateli.setDistrict("Mahendragarh");
        s_stop_mnd_ateli.setLatitude(28.102);
        s_stop_mnd_ateli.setLongitude(76.261);
        s_stop_mnd_ateli = busStopRepository.save(s_stop_mnd_ateli);
        stopMap.put("stop-mnd-ateli", s_stop_mnd_ateli);

        BusStop s_stop_mnd_kanina = new BusStop();
        s_stop_mnd_kanina.setName("Kanina Khas Bus Stand");
        s_stop_mnd_kanina.setDistrict("Mahendragarh");
        s_stop_mnd_kanina.setLatitude(28.324);
        s_stop_mnd_kanina.setLongitude(76.315);
        s_stop_mnd_kanina = busStopRepository.save(s_stop_mnd_kanina);
        stopMap.put("stop-mnd-kanina", s_stop_mnd_kanina);

        BusStop s_stop_nuh_stand = new BusStop();
        s_stop_nuh_stand.setName("Nuh Main Bus Stand");
        s_stop_nuh_stand.setDistrict("Nuh");
        s_stop_nuh_stand.setLatitude(28.112);
        s_stop_nuh_stand.setLongitude(77.015);
        s_stop_nuh_stand = busStopRepository.save(s_stop_nuh_stand);
        stopMap.put("stop-nuh-stand", s_stop_nuh_stand);

        BusStop s_stop_nuh_taoru = new BusStop();
        s_stop_nuh_taoru.setName("Taoru Bus Stand");
        s_stop_nuh_taoru.setDistrict("Nuh");
        s_stop_nuh_taoru.setLatitude(28.215);
        s_stop_nuh_taoru.setLongitude(76.952);
        s_stop_nuh_taoru = busStopRepository.save(s_stop_nuh_taoru);
        stopMap.put("stop-nuh-taoru", s_stop_nuh_taoru);

        BusStop s_stop_nuh_ferozepur = new BusStop();
        s_stop_nuh_ferozepur.setName("Ferozepur Jhirka Bus Stand");
        s_stop_nuh_ferozepur.setDistrict("Nuh");
        s_stop_nuh_ferozepur.setLatitude(27.795);
        s_stop_nuh_ferozepur.setLongitude(76.948);
        s_stop_nuh_ferozepur = busStopRepository.save(s_stop_nuh_ferozepur);
        stopMap.put("stop-nuh-ferozepur", s_stop_nuh_ferozepur);

        BusStop s_stop_nuh_punhana = new BusStop();
        s_stop_nuh_punhana.setName("Punhana Bus Stop");
        s_stop_nuh_punhana.setDistrict("Nuh");
        s_stop_nuh_punhana.setLatitude(27.868);
        s_stop_nuh_punhana.setLongitude(77.202);
        s_stop_nuh_punhana = busStopRepository.save(s_stop_nuh_punhana);
        stopMap.put("stop-nuh-punhana", s_stop_nuh_punhana);

        BusStop s_stop_pal_stand = new BusStop();
        s_stop_pal_stand.setName("Palwal Main Bus Stand");
        s_stop_pal_stand.setDistrict("Palwal");
        s_stop_pal_stand.setLatitude(28.144);
        s_stop_pal_stand.setLongitude(77.328);
        s_stop_pal_stand = busStopRepository.save(s_stop_pal_stand);
        stopMap.put("stop-pal-stand", s_stop_pal_stand);

        BusStop s_stop_pal_hodal = new BusStop();
        s_stop_pal_hodal.setName("Hodal Bus Stand");
        s_stop_pal_hodal.setDistrict("Palwal");
        s_stop_pal_hodal.setLatitude(27.895);
        s_stop_pal_hodal.setLongitude(77.371);
        s_stop_pal_hodal = busStopRepository.save(s_stop_pal_hodal);
        stopMap.put("stop-pal-hodal", s_stop_pal_hodal);

        BusStop s_stop_pal_hathin = new BusStop();
        s_stop_pal_hathin.setName("Hathin Bus Stand");
        s_stop_pal_hathin.setDistrict("Palwal");
        s_stop_pal_hathin.setLatitude(28.052);
        s_stop_pal_hathin.setLongitude(77.241);
        s_stop_pal_hathin = busStopRepository.save(s_stop_pal_hathin);
        stopMap.put("stop-pal-hathin", s_stop_pal_hathin);

        BusStop s_stop_pkl_sec5 = new BusStop();
        s_stop_pkl_sec5.setName("Panchkula Sector 5 Terminal");
        s_stop_pkl_sec5.setDistrict("Panchkula");
        s_stop_pkl_sec5.setLatitude(30.6942);
        s_stop_pkl_sec5.setLongitude(76.8606);
        s_stop_pkl_sec5 = busStopRepository.save(s_stop_pkl_sec5);
        stopMap.put("stop-pkl-sec5", s_stop_pkl_sec5);

        BusStop s_stop_pkl_pinjore = new BusStop();
        s_stop_pkl_pinjore.setName("Pinjore Bus Stand");
        s_stop_pkl_pinjore.setDistrict("Panchkula");
        s_stop_pkl_pinjore.setLatitude(30.796);
        s_stop_pkl_pinjore.setLongitude(76.915);
        s_stop_pkl_pinjore = busStopRepository.save(s_stop_pkl_pinjore);
        stopMap.put("stop-pkl-pinjore", s_stop_pkl_pinjore);

        BusStop s_stop_pkl_kalka = new BusStop();
        s_stop_pkl_kalka.setName("Kalka Railway Station Stop");
        s_stop_pkl_kalka.setDistrict("Panchkula");
        s_stop_pkl_kalka.setLatitude(30.835);
        s_stop_pkl_kalka.setLongitude(76.936);
        s_stop_pkl_kalka = busStopRepository.save(s_stop_pkl_kalka);
        stopMap.put("stop-pkl-kalka", s_stop_pkl_kalka);

        BusStop s_stop_pan_stand = new BusStop();
        s_stop_pan_stand.setName("Panipat General Bus Stand");
        s_stop_pan_stand.setDistrict("Panipat");
        s_stop_pan_stand.setLatitude(29.3909);
        s_stop_pan_stand.setLongitude(76.9635);
        s_stop_pan_stand = busStopRepository.save(s_stop_pan_stand);
        stopMap.put("stop-pan-stand", s_stop_pan_stand);

        BusStop s_stop_pan_toll = new BusStop();
        s_stop_pan_toll.setName("Panipat Toll Plaza GT Road");
        s_stop_pan_toll.setDistrict("Panipat");
        s_stop_pan_toll.setLatitude(29.42);
        s_stop_pan_toll.setLongitude(76.975);
        s_stop_pan_toll = busStopRepository.save(s_stop_pan_toll);
        stopMap.put("stop-pan-toll", s_stop_pan_toll);

        BusStop s_stop_samalkha = new BusStop();
        s_stop_samalkha.setName("Samalkha Stop");
        s_stop_samalkha.setDistrict("Panipat");
        s_stop_samalkha.setLatitude(29.23);
        s_stop_samalkha.setLongitude(77.01);
        s_stop_samalkha = busStopRepository.save(s_stop_samalkha);
        stopMap.put("stop-samalkha", s_stop_samalkha);

        BusStop s_stop_pan_israna = new BusStop();
        s_stop_pan_israna.setName("Israna Bus Stand");
        s_stop_pan_israna.setDistrict("Panipat");
        s_stop_pan_israna.setLatitude(29.278);
        s_stop_pan_israna.setLongitude(76.845);
        s_stop_pan_israna = busStopRepository.save(s_stop_pan_israna);
        stopMap.put("stop-pan-israna", s_stop_pan_israna);

        BusStop s_stop_rew_stand = new BusStop();
        s_stop_rew_stand.setName("Rewari General Bus Stand");
        s_stop_rew_stand.setDistrict("Rewari");
        s_stop_rew_stand.setLatitude(28.183);
        s_stop_rew_stand.setLongitude(76.618);
        s_stop_rew_stand = busStopRepository.save(s_stop_rew_stand);
        stopMap.put("stop-rew-stand", s_stop_rew_stand);

        BusStop s_stop_dharuhera = new BusStop();
        s_stop_dharuhera.setName("Dharuhera Bus Stop");
        s_stop_dharuhera.setDistrict("Rewari");
        s_stop_dharuhera.setLatitude(28.205);
        s_stop_dharuhera.setLongitude(76.792);
        s_stop_dharuhera = busStopRepository.save(s_stop_dharuhera);
        stopMap.put("stop-dharuhera", s_stop_dharuhera);

        BusStop s_stop_bawal = new BusStop();
        s_stop_bawal.setName("Bawal IMT Chowk");
        s_stop_bawal.setDistrict("Rewari");
        s_stop_bawal.setLatitude(28.082);
        s_stop_bawal.setLongitude(76.582);
        s_stop_bawal = busStopRepository.save(s_stop_bawal);
        stopMap.put("stop-bawal", s_stop_bawal);

        BusStop s_stop_rew_kosli = new BusStop();
        s_stop_rew_kosli.setName("Kosli Bus Stand");
        s_stop_rew_kosli.setDistrict("Rewari");
        s_stop_rew_kosli.setLatitude(28.411);
        s_stop_rew_kosli.setLongitude(76.482);
        s_stop_rew_kosli = busStopRepository.save(s_stop_rew_kosli);
        stopMap.put("stop-rew-kosli", s_stop_rew_kosli);

        BusStop s_stop_rtk_stand = new BusStop();
        s_stop_rtk_stand.setName("Rohtak New Bus Stand");
        s_stop_rtk_stand.setDistrict("Rohtak");
        s_stop_rtk_stand.setLatitude(28.8955);
        s_stop_rtk_stand.setLongitude(76.6066);
        s_stop_rtk_stand = busStopRepository.save(s_stop_rtk_stand);
        stopMap.put("stop-rtk-stand", s_stop_rtk_stand);

        BusStop s_stop_rtk_mdu = new BusStop();
        s_stop_rtk_mdu.setName("MDU Gate, Rohtak");
        s_stop_rtk_mdu.setDistrict("Rohtak");
        s_stop_rtk_mdu.setLatitude(28.882);
        s_stop_rtk_mdu.setLongitude(76.618);
        s_stop_rtk_mdu = busStopRepository.save(s_stop_rtk_mdu);
        stopMap.put("stop-rtk-mdu", s_stop_rtk_mdu);

        BusStop s_stop_sampla = new BusStop();
        s_stop_sampla.setName("Sampla Chowk");
        s_stop_sampla.setDistrict("Rohtak");
        s_stop_sampla.setLatitude(28.775);
        s_stop_sampla.setLongitude(76.772);
        s_stop_sampla = busStopRepository.save(s_stop_sampla);
        stopMap.put("stop-sampla", s_stop_sampla);

        BusStop s_stop_kalanaur = new BusStop();
        s_stop_kalanaur.setName("Kalanaur Town Stop");
        s_stop_kalanaur.setDistrict("Rohtak");
        s_stop_kalanaur.setLatitude(28.825);
        s_stop_kalanaur.setLongitude(76.402);
        s_stop_kalanaur = busStopRepository.save(s_stop_kalanaur);
        stopMap.put("stop-kalanaur", s_stop_kalanaur);

        BusStop s_stop_meham = new BusStop();
        s_stop_meham.setName("Meham Chowk");
        s_stop_meham.setDistrict("Rohtak");
        s_stop_meham.setLatitude(28.965);
        s_stop_meham.setLongitude(76.295);
        s_stop_meham = busStopRepository.save(s_stop_meham);
        stopMap.put("stop-meham", s_stop_meham);

        BusStop s_stop_sir_stand = new BusStop();
        s_stop_sir_stand.setName("Sirsa General Bus Stand");
        s_stop_sir_stand.setDistrict("Sirsa");
        s_stop_sir_stand.setLatitude(29.534);
        s_stop_sir_stand.setLongitude(75.032);
        s_stop_sir_stand = busStopRepository.save(s_stop_sir_stand);
        stopMap.put("stop-sir-stand", s_stop_sir_stand);

        BusStop s_stop_sir_dabwali = new BusStop();
        s_stop_sir_dabwali.setName("Mandi Dabwali Bus Stand");
        s_stop_sir_dabwali.setDistrict("Sirsa");
        s_stop_sir_dabwali.setLatitude(29.957);
        s_stop_sir_dabwali.setLongitude(74.721);
        s_stop_sir_dabwali = busStopRepository.save(s_stop_sir_dabwali);
        stopMap.put("stop-sir-dabwali", s_stop_sir_dabwali);

        BusStop s_stop_sir_ellenabad = new BusStop();
        s_stop_sir_ellenabad.setName("Ellenabad Bus Stand");
        s_stop_sir_ellenabad.setDistrict("Sirsa");
        s_stop_sir_ellenabad.setLatitude(29.452);
        s_stop_sir_ellenabad.setLongitude(74.658);
        s_stop_sir_ellenabad = busStopRepository.save(s_stop_sir_ellenabad);
        stopMap.put("stop-sir-ellenabad", s_stop_sir_ellenabad);

        BusStop s_stop_sir_rania = new BusStop();
        s_stop_sir_rania.setName("Rania Bus Stop");
        s_stop_sir_rania.setDistrict("Sirsa");
        s_stop_sir_rania.setLatitude(29.525);
        s_stop_sir_rania.setLongitude(74.835);
        s_stop_sir_rania = busStopRepository.save(s_stop_sir_rania);
        stopMap.put("stop-sir-rania", s_stop_sir_rania);

        BusStop s_stop_snp_stand = new BusStop();
        s_stop_snp_stand.setName("Sonipat New Bus Stand");
        s_stop_snp_stand.setDistrict("Sonipat");
        s_stop_snp_stand.setLatitude(28.995);
        s_stop_snp_stand.setLongitude(77.012);
        s_stop_snp_stand = busStopRepository.save(s_stop_snp_stand);
        stopMap.put("stop-snp-stand", s_stop_snp_stand);

        BusStop s_stop_murthal = new BusStop();
        s_stop_murthal.setName("Murthal Dhabas GT Road");
        s_stop_murthal.setDistrict("Sonipat");
        s_stop_murthal.setLatitude(29.025);
        s_stop_murthal.setLongitude(77.07);
        s_stop_murthal = busStopRepository.save(s_stop_murthal);
        stopMap.put("stop-murthal", s_stop_murthal);

        BusStop s_stop_snp_gohana = new BusStop();
        s_stop_snp_gohana.setName("Gohana Bus Stand");
        s_stop_snp_gohana.setDistrict("Sonipat");
        s_stop_snp_gohana.setLatitude(29.135);
        s_stop_snp_gohana.setLongitude(76.702);
        s_stop_snp_gohana = busStopRepository.save(s_stop_snp_gohana);
        stopMap.put("stop-snp-gohana", s_stop_snp_gohana);

        BusStop s_stop_snp_ganaur = new BusStop();
        s_stop_snp_ganaur.setName("Ganaur Stop");
        s_stop_snp_ganaur.setDistrict("Sonipat");
        s_stop_snp_ganaur.setLatitude(29.132);
        s_stop_snp_ganaur.setLongitude(77.021);
        s_stop_snp_ganaur = busStopRepository.save(s_stop_snp_ganaur);
        stopMap.put("stop-snp-ganaur", s_stop_snp_ganaur);

        BusStop s_stop_snp_kundli = new BusStop();
        s_stop_snp_kundli.setName("Kundli Border Terminal");
        s_stop_snp_kundli.setDistrict("Sonipat");
        s_stop_snp_kundli.setLatitude(28.878);
        s_stop_snp_kundli.setLongitude(77.121);
        s_stop_snp_kundli = busStopRepository.save(s_stop_snp_kundli);
        stopMap.put("stop-snp-kundli", s_stop_snp_kundli);

        BusStop s_stop_ymn_stand = new BusStop();
        s_stop_ymn_stand.setName("Yamunanagar Bus Stand");
        s_stop_ymn_stand.setDistrict("Yamunanagar");
        s_stop_ymn_stand.setLatitude(30.129);
        s_stop_ymn_stand.setLongitude(77.267);
        s_stop_ymn_stand = busStopRepository.save(s_stop_ymn_stand);
        stopMap.put("stop-ymn-stand", s_stop_ymn_stand);

        BusStop s_stop_ymn_jagadhri = new BusStop();
        s_stop_ymn_jagadhri.setName("Jagadhri Workshop Bus Stand");
        s_stop_ymn_jagadhri.setDistrict("Yamunanagar");
        s_stop_ymn_jagadhri.setLatitude(30.172);
        s_stop_ymn_jagadhri.setLongitude(77.301);
        s_stop_ymn_jagadhri = busStopRepository.save(s_stop_ymn_jagadhri);
        stopMap.put("stop-ymn-jagadhri", s_stop_ymn_jagadhri);

        BusStop s_stop_ymn_radaur = new BusStop();
        s_stop_ymn_radaur.setName("Radaur Bus Stand");
        s_stop_ymn_radaur.setDistrict("Yamunanagar");
        s_stop_ymn_radaur.setLatitude(30.028);
        s_stop_ymn_radaur.setLongitude(77.155);
        s_stop_ymn_radaur = busStopRepository.save(s_stop_ymn_radaur);
        stopMap.put("stop-ymn-radaur", s_stop_ymn_radaur);

        BusStop s_stop_ymn_bilaspur = new BusStop();
        s_stop_ymn_bilaspur.setName("Bilaspur Stop");
        s_stop_ymn_bilaspur.setDistrict("Yamunanagar");
        s_stop_ymn_bilaspur.setLatitude(30.302);
        s_stop_ymn_bilaspur.setLongitude(77.322);
        s_stop_ymn_bilaspur = busStopRepository.save(s_stop_ymn_bilaspur);
        stopMap.put("stop-ymn-bilaspur", s_stop_ymn_bilaspur);

        BusStop s_stop_chd_17 = new BusStop();
        s_stop_chd_17.setName("Chandigarh ISBT Sector 17");
        s_stop_chd_17.setDistrict("Chandigarh");
        s_stop_chd_17.setLatitude(30.7398);
        s_stop_chd_17.setLongitude(76.7827);
        s_stop_chd_17 = busStopRepository.save(s_stop_chd_17);
        stopMap.put("stop-chd-17", s_stop_chd_17);

        BusStop s_stop_chd_43 = new BusStop();
        s_stop_chd_43.setName("Chandigarh ISBT Sector 43");
        s_stop_chd_43.setDistrict("Chandigarh");
        s_stop_chd_43.setLatitude(30.716);
        s_stop_chd_43.setLongitude(76.745);
        s_stop_chd_43 = busStopRepository.save(s_stop_chd_43);
        stopMap.put("stop-chd-43", s_stop_chd_43);

        BusStop s_stop_zirakpur = new BusStop();
        s_stop_zirakpur.setName("Zirakpur Flyover Junction");
        s_stop_zirakpur.setDistrict("Chandigarh");
        s_stop_zirakpur.setLatitude(30.642);
        s_stop_zirakpur.setLongitude(76.818);
        s_stop_zirakpur = busStopRepository.save(s_stop_zirakpur);
        stopMap.put("stop-zirakpur", s_stop_zirakpur);

        BusStop s_stop_del_isbt_kg = new BusStop();
        s_stop_del_isbt_kg.setName("Delhi Kashmere Gate ISBT");
        s_stop_del_isbt_kg.setDistrict("Delhi NCR");
        s_stop_del_isbt_kg.setLatitude(28.6675);
        s_stop_del_isbt_kg.setLongitude(77.2285);
        s_stop_del_isbt_kg = busStopRepository.save(s_stop_del_isbt_kg);
        stopMap.put("stop-del-isbt-kg", s_stop_del_isbt_kg);

        BusStop s_stop_del_dhaula_kuan = new BusStop();
        s_stop_del_dhaula_kuan.setName("Dhaula Kuan, Delhi");
        s_stop_del_dhaula_kuan.setDistrict("Delhi NCR");
        s_stop_del_dhaula_kuan.setLatitude(28.592);
        s_stop_del_dhaula_kuan.setLongitude(77.161);
        s_stop_del_dhaula_kuan = busStopRepository.save(s_stop_del_dhaula_kuan);
        stopMap.put("stop-del-dhaula-kuan", s_stop_del_dhaula_kuan);

        BusStop s_stop_del_skk = new BusStop();
        s_stop_del_skk.setName("Sarai Kale Khan ISBT, Delhi");
        s_stop_del_skk.setDistrict("Delhi NCR");
        s_stop_del_skk.setLatitude(28.589);
        s_stop_del_skk.setLongitude(77.256);
        s_stop_del_skk = busStopRepository.save(s_stop_del_skk);
        stopMap.put("stop-del-skk", s_stop_del_skk);

        BusStop s_stop_del_anand_vihar = new BusStop();
        s_stop_del_anand_vihar.setName("Anand Vihar ISBT, Delhi");
        s_stop_del_anand_vihar.setDistrict("Delhi NCR");
        s_stop_del_anand_vihar.setLatitude(28.648);
        s_stop_del_anand_vihar.setLongitude(77.315);
        s_stop_del_anand_vihar = busStopRepository.save(s_stop_del_anand_vihar);
        stopMap.put("stop-del-anand-vihar", s_stop_del_anand_vihar);

        Map<String, Route> routeMap = new HashMap<>();

        Route r_route_ggn_chd_01 = new Route();
        r_route_ggn_chd_01.setRouteNumber("HR-VOLVO-01");
        r_route_ggn_chd_01.setSource("Gurugram General Bus Stand");
        r_route_ggn_chd_01.setDestination("Chandigarh ISBT Sector 17");
        r_route_ggn_chd_01.setTotalDistance(278.0);
        r_route_ggn_chd_01 = routeRepository.save(r_route_ggn_chd_01);
        routeMap.put("route-ggn-chd-01", r_route_ggn_chd_01);

        Route r_route_rtk_del_02 = new Route();
        r_route_rtk_del_02.setRouteNumber("HR-ORD-02");
        r_route_rtk_del_02.setSource("Rohtak New Bus Stand");
        r_route_rtk_del_02.setDestination("Delhi Kashmere Gate ISBT");
        r_route_rtk_del_02.setTotalDistance(72.5);
        r_route_rtk_del_02 = routeRepository.save(r_route_rtk_del_02);
        routeMap.put("route-rtk-del-02", r_route_rtk_del_02);

        Route r_route_hsr_del_03 = new Route();
        r_route_hsr_del_03.setRouteNumber("HR-EXP-03");
        r_route_hsr_del_03.setSource("Hisar New Bus Stand");
        r_route_hsr_del_03.setDestination("Delhi Kashmere Gate ISBT");
        r_route_hsr_del_03.setTotalDistance(168.0);
        r_route_hsr_del_03 = routeRepository.save(r_route_hsr_del_03);
        routeMap.put("route-hsr-del-03", r_route_hsr_del_03);

        Route r_route_bhw_rtk_04 = new Route();
        r_route_bhw_rtk_04.setRouteNumber("HR-ORD-04");
        r_route_bhw_rtk_04.setSource("Bhiwani General Bus Stand");
        r_route_bhw_rtk_04.setDestination("Rohtak New Bus Stand");
        r_route_bhw_rtk_04.setTotalDistance(48.0);
        r_route_bhw_rtk_04 = routeRepository.save(r_route_bhw_rtk_04);
        routeMap.put("route-bhw-rtk-04", r_route_bhw_rtk_04);

        Route r_route_cd_bhw_05 = new Route();
        r_route_cd_bhw_05.setRouteNumber("HR-ORD-05");
        r_route_cd_bhw_05.setSource("Charkhi Dadri Main Bus Stand");
        r_route_cd_bhw_05.setDestination("Bhiwani General Bus Stand");
        r_route_cd_bhw_05.setTotalDistance(31.0);
        r_route_cd_bhw_05 = routeRepository.save(r_route_cd_bhw_05);
        routeMap.put("route-cd-bhw-05", r_route_cd_bhw_05);

        Route r_route_pan_amb_06 = new Route();
        r_route_pan_amb_06.setRouteNumber("HR-EXP-06");
        r_route_pan_amb_06.setSource("Panipat General Bus Stand");
        r_route_pan_amb_06.setDestination("Ambala Cantt General Bus Stand");
        r_route_pan_amb_06.setTotalDistance(115.0);
        r_route_pan_amb_06 = routeRepository.save(r_route_pan_amb_06);
        routeMap.put("route-pan-amb-06", r_route_pan_amb_06);

        Route r_route_rew_ggn_07 = new Route();
        r_route_rew_ggn_07.setRouteNumber("HR-ORD-07");
        r_route_rew_ggn_07.setSource("Rewari General Bus Stand");
        r_route_rew_ggn_07.setDestination("Gurugram General Bus Stand");
        r_route_rew_ggn_07.setTotalDistance(52.0);
        r_route_rew_ggn_07 = routeRepository.save(r_route_rew_ggn_07);
        routeMap.put("route-rew-ggn-07", r_route_rew_ggn_07);

        Route r_route_fbd_del_08 = new Route();
        r_route_fbd_del_08.setRouteNumber("HR-EXP-08");
        r_route_fbd_del_08.setSource("Faridabad NIT Bus Stand");
        r_route_fbd_del_08.setDestination("Sarai Kale Khan ISBT, Delhi");
        r_route_fbd_del_08.setTotalDistance(34.0);
        r_route_fbd_del_08 = routeRepository.save(r_route_fbd_del_08);
        routeMap.put("route-fbd-del-08", r_route_fbd_del_08);

        Route r_route_amb_chd_09 = new Route();
        r_route_amb_chd_09.setRouteNumber("HR-EXP-09");
        r_route_amb_chd_09.setSource("Ambala Cantt General Bus Stand");
        r_route_amb_chd_09.setDestination("Chandigarh ISBT Sector 17");
        r_route_amb_chd_09.setTotalDistance(46.0);
        r_route_amb_chd_09 = routeRepository.save(r_route_amb_chd_09);
        routeMap.put("route-amb-chd-09", r_route_amb_chd_09);

        Route r_route_sir_hsr_10 = new Route();
        r_route_sir_hsr_10.setRouteNumber("HR-EXP-10");
        r_route_sir_hsr_10.setSource("Sirsa General Bus Stand");
        r_route_sir_hsr_10.setDestination("Hisar New Bus Stand");
        r_route_sir_hsr_10.setTotalDistance(88.0);
        r_route_sir_hsr_10 = routeRepository.save(r_route_sir_hsr_10);
        routeMap.put("route-sir-hsr-10", r_route_sir_hsr_10);

        Route r_route_jnd_chd_11 = new Route();
        r_route_jnd_chd_11.setRouteNumber("HR-EXP-11");
        r_route_jnd_chd_11.setSource("Jind General Bus Stand");
        r_route_jnd_chd_11.setDestination("Chandigarh ISBT Sector 17");
        r_route_jnd_chd_11.setTotalDistance(165.0);
        r_route_jnd_chd_11 = routeRepository.save(r_route_jnd_chd_11);
        routeMap.put("route-jnd-chd-11", r_route_jnd_chd_11);

        Route r_route_kth_krl_12 = new Route();
        r_route_kth_krl_12.setRouteNumber("HR-ORD-12");
        r_route_kth_krl_12.setSource("Kaithal New Bus Stand");
        r_route_kth_krl_12.setDestination("Karnal New Bus Stand");
        r_route_kth_krl_12.setTotalDistance(62.0);
        r_route_kth_krl_12 = routeRepository.save(r_route_kth_krl_12);
        routeMap.put("route-kth-krl-12", r_route_kth_krl_12);

        Route r_route_krl_ymn_13 = new Route();
        r_route_krl_ymn_13.setRouteNumber("HR-ORD-13");
        r_route_krl_ymn_13.setSource("Karnal New Bus Stand");
        r_route_krl_ymn_13.setDestination("Yamunanagar Bus Stand");
        r_route_krl_ymn_13.setTotalDistance(68.0);
        r_route_krl_ymn_13 = routeRepository.save(r_route_krl_ymn_13);
        routeMap.put("route-krl-ymn-13", r_route_krl_ymn_13);

        Route r_route_ymn_chd_14 = new Route();
        r_route_ymn_chd_14.setRouteNumber("HR-EXP-14");
        r_route_ymn_chd_14.setSource("Yamunanagar Bus Stand");
        r_route_ymn_chd_14.setDestination("Chandigarh ISBT Sector 17");
        r_route_ymn_chd_14.setTotalDistance(105.0);
        r_route_ymn_chd_14 = routeRepository.save(r_route_ymn_chd_14);
        routeMap.put("route-ymn-chd-14", r_route_ymn_chd_14);

        Route r_route_pkl_amb_15 = new Route();
        r_route_pkl_amb_15.setRouteNumber("HR-ORD-15");
        r_route_pkl_amb_15.setSource("Panchkula Sector 5 Terminal");
        r_route_pkl_amb_15.setDestination("Ambala Cantt General Bus Stand");
        r_route_pkl_amb_15.setTotalDistance(48.0);
        r_route_pkl_amb_15 = routeRepository.save(r_route_pkl_amb_15);
        routeMap.put("route-pkl-amb-15", r_route_pkl_amb_15);

        Route r_route_mnd_del_16 = new Route();
        r_route_mnd_del_16.setRouteNumber("HR-EXP-16");
        r_route_mnd_del_16.setSource("Narnaul General Bus Stand");
        r_route_mnd_del_16.setDestination("Delhi Kashmere Gate ISBT");
        r_route_mnd_del_16.setTotalDistance(142.0);
        r_route_mnd_del_16 = routeRepository.save(r_route_mnd_del_16);
        routeMap.put("route-mnd-del-16", r_route_mnd_del_16);

        Route r_route_cd_rew_17 = new Route();
        r_route_cd_rew_17.setRouteNumber("HR-ORD-17");
        r_route_cd_rew_17.setSource("Charkhi Dadri Main Bus Stand");
        r_route_cd_rew_17.setDestination("Rewari General Bus Stand");
        r_route_cd_rew_17.setTotalDistance(68.0);
        r_route_cd_rew_17 = routeRepository.save(r_route_cd_rew_17);
        routeMap.put("route-cd-rew-17", r_route_cd_rew_17);

        Route r_route_jhj_del_18 = new Route();
        r_route_jhj_del_18.setRouteNumber("HR-ORD-18");
        r_route_jhj_del_18.setSource("Jhajjar Main Bus Stand");
        r_route_jhj_del_18.setDestination("Delhi Kashmere Gate ISBT");
        r_route_jhj_del_18.setTotalDistance(64.0);
        r_route_jhj_del_18 = routeRepository.save(r_route_jhj_del_18);
        routeMap.put("route-jhj-del-18", r_route_jhj_del_18);

        Route r_route_snp_del_19 = new Route();
        r_route_snp_del_19.setRouteNumber("HR-ORD-19");
        r_route_snp_del_19.setSource("Sonipat New Bus Stand");
        r_route_snp_del_19.setDestination("Delhi Kashmere Gate ISBT");
        r_route_snp_del_19.setTotalDistance(48.0);
        r_route_snp_del_19 = routeRepository.save(r_route_snp_del_19);
        routeMap.put("route-snp-del-19", r_route_snp_del_19);

        Route r_route_snp_goh_20 = new Route();
        r_route_snp_goh_20.setRouteNumber("HR-ORD-20");
        r_route_snp_goh_20.setSource("Sonipat New Bus Stand");
        r_route_snp_goh_20.setDestination("Rohtak New Bus Stand");
        r_route_snp_goh_20.setTotalDistance(72.0);
        r_route_snp_goh_20 = routeRepository.save(r_route_snp_goh_20);
        routeMap.put("route-snp-goh-20", r_route_snp_goh_20);

        Route r_route_nuh_ggn_21 = new Route();
        r_route_nuh_ggn_21.setRouteNumber("HR-ORD-21");
        r_route_nuh_ggn_21.setSource("Nuh Main Bus Stand");
        r_route_nuh_ggn_21.setDestination("Gurugram General Bus Stand");
        r_route_nuh_ggn_21.setTotalDistance(46.0);
        r_route_nuh_ggn_21 = routeRepository.save(r_route_nuh_ggn_21);
        routeMap.put("route-nuh-ggn-21", r_route_nuh_ggn_21);

        Route r_route_pal_del_22 = new Route();
        r_route_pal_del_22.setRouteNumber("HR-ORD-22");
        r_route_pal_del_22.setSource("Palwal Main Bus Stand");
        r_route_pal_del_22.setDestination("Sarai Kale Khan ISBT, Delhi");
        r_route_pal_del_22.setTotalDistance(58.0);
        r_route_pal_del_22 = routeRepository.save(r_route_pal_del_22);
        routeMap.put("route-pal-del-22", r_route_pal_del_22);

        Route r_route_fat_chd_23 = new Route();
        r_route_fat_chd_23.setRouteNumber("HR-EXP-23");
        r_route_fat_chd_23.setSource("Fatehabad Bus Stand");
        r_route_fat_chd_23.setDestination("Chandigarh ISBT Sector 17");
        r_route_fat_chd_23.setTotalDistance(210.0);
        r_route_fat_chd_23 = routeRepository.save(r_route_fat_chd_23);
        routeMap.put("route-fat-chd-23", r_route_fat_chd_23);

        Route r_route_krk_del_24 = new Route();
        r_route_krk_del_24.setRouteNumber("HR-EXP-24");
        r_route_krk_del_24.setSource("Pipli Bus Stand (Kurukshetra)");
        r_route_krk_del_24.setDestination("Delhi Kashmere Gate ISBT");
        r_route_krk_del_24.setTotalDistance(158.0);
        r_route_krk_del_24 = routeRepository.save(r_route_krk_del_24);
        routeMap.put("route-krk-del-24", r_route_krk_del_24);

        Route r_route_hsr_chd_25 = new Route();
        r_route_hsr_chd_25.setRouteNumber("HR-EXP-25");
        r_route_hsr_chd_25.setSource("Hisar New Bus Stand");
        r_route_hsr_chd_25.setDestination("Chandigarh ISBT Sector 17");
        r_route_hsr_chd_25.setTotalDistance(235.0);
        r_route_hsr_chd_25 = routeRepository.save(r_route_hsr_chd_25);
        routeMap.put("route-hsr-chd-25", r_route_hsr_chd_25);

        Route r_route_bhw_del_26 = new Route();
        r_route_bhw_del_26.setRouteNumber("HR-EXP-26");
        r_route_bhw_del_26.setSource("Bhiwani General Bus Stand");
        r_route_bhw_del_26.setDestination("Delhi Kashmere Gate ISBT");
        r_route_bhw_del_26.setTotalDistance(120.0);
        r_route_bhw_del_26 = routeRepository.save(r_route_bhw_del_26);
        routeMap.put("route-bhw-del-26", r_route_bhw_del_26);

        Route r_route_sir_del_27 = new Route();
        r_route_sir_del_27.setRouteNumber("HR-EXP-27");
        r_route_sir_del_27.setSource("Sirsa General Bus Stand");
        r_route_sir_del_27.setDestination("Delhi Kashmere Gate ISBT");
        r_route_sir_del_27.setTotalDistance(256.0);
        r_route_sir_del_27 = routeRepository.save(r_route_sir_del_27);
        routeMap.put("route-sir-del-27", r_route_sir_del_27);

        Route r_route_pal_fbd_28 = new Route();
        r_route_pal_fbd_28.setRouteNumber("HR-ORD-28");
        r_route_pal_fbd_28.setSource("Palwal Main Bus Stand");
        r_route_pal_fbd_28.setDestination("Faridabad NIT Bus Stand");
        r_route_pal_fbd_28.setTotalDistance(28.0);
        r_route_pal_fbd_28 = routeRepository.save(r_route_pal_fbd_28);
        routeMap.put("route-pal-fbd-28", r_route_pal_fbd_28);

        Route r_route_nuh_pal_29 = new Route();
        r_route_nuh_pal_29.setRouteNumber("HR-ORD-29");
        r_route_nuh_pal_29.setSource("Nuh Main Bus Stand");
        r_route_nuh_pal_29.setDestination("Palwal Main Bus Stand");
        r_route_nuh_pal_29.setTotalDistance(35.0);
        r_route_nuh_pal_29 = routeRepository.save(r_route_nuh_pal_29);
        routeMap.put("route-nuh-pal-29", r_route_nuh_pal_29);

        Route r_route_mnd_bhw_30 = new Route();
        r_route_mnd_bhw_30.setRouteNumber("HR-ORD-30");
        r_route_mnd_bhw_30.setSource("Narnaul General Bus Stand");
        r_route_mnd_bhw_30.setDestination("Bhiwani General Bus Stand");
        r_route_mnd_bhw_30.setTotalDistance(88.0);
        r_route_mnd_bhw_30 = routeRepository.save(r_route_mnd_bhw_30);
        routeMap.put("route-mnd-bhw-30", r_route_mnd_bhw_30);

        Map<String, Bus> busMap = new HashMap<>();

        Bus b_bus_gur_1000 = new Bus();
        b_bus_gur_1000.setBusNumber("HR-26-G-1000");
        b_bus_gur_1000.setRegistrationNumber("HR-26-G-1000");
        b_bus_gur_1000.setBusType(BusType.ORDINARY);
        b_bus_gur_1000.setCapacity(45);
        b_bus_gur_1000.setStatus(BusStatus.ACTIVE);
        b_bus_gur_1000 = busRepository.save(b_bus_gur_1000);
        busMap.put("bus-gur-1000", b_bus_gur_1000);

        Bus b_bus_roh_1037 = new Bus();
        b_bus_roh_1037.setBusNumber("HR-12-G-1037");
        b_bus_roh_1037.setRegistrationNumber("HR-12-G-1037");
        b_bus_roh_1037.setBusType(BusType.ORDINARY);
        b_bus_roh_1037.setCapacity(52);
        b_bus_roh_1037.setStatus(BusStatus.ACTIVE);
        b_bus_roh_1037 = busRepository.save(b_bus_roh_1037);
        busMap.put("bus-roh-1037", b_bus_roh_1037);

        Bus b_bus_his_1074 = new Bus();
        b_bus_his_1074.setBusNumber("HR-20-G-1074");
        b_bus_his_1074.setRegistrationNumber("HR-20-G-1074");
        b_bus_his_1074.setBusType(BusType.ORDINARY);
        b_bus_his_1074.setCapacity(52);
        b_bus_his_1074.setStatus(BusStatus.ACTIVE);
        b_bus_his_1074 = busRepository.save(b_bus_his_1074);
        busMap.put("bus-his-1074", b_bus_his_1074);

        Bus b_bus_bhi_1111 = new Bus();
        b_bus_bhi_1111.setBusNumber("HR-16-G-1111");
        b_bus_bhi_1111.setRegistrationNumber("HR-16-G-1111");
        b_bus_bhi_1111.setBusType(BusType.ORDINARY);
        b_bus_bhi_1111.setCapacity(52);
        b_bus_bhi_1111.setStatus(BusStatus.ACTIVE);
        b_bus_bhi_1111 = busRepository.save(b_bus_bhi_1111);
        busMap.put("bus-bhi-1111", b_bus_bhi_1111);

        Bus b_bus_cha_1148 = new Bus();
        b_bus_cha_1148.setBusNumber("HR-19-G-1148");
        b_bus_cha_1148.setRegistrationNumber("HR-19-G-1148");
        b_bus_cha_1148.setBusType(BusType.ORDINARY);
        b_bus_cha_1148.setCapacity(52);
        b_bus_cha_1148.setStatus(BusStatus.ACTIVE);
        b_bus_cha_1148 = busRepository.save(b_bus_cha_1148);
        busMap.put("bus-cha-1148", b_bus_cha_1148);

        Bus b_bus_pan_1185 = new Bus();
        b_bus_pan_1185.setBusNumber("HR-06-G-1185");
        b_bus_pan_1185.setRegistrationNumber("HR-06-G-1185");
        b_bus_pan_1185.setBusType(BusType.ORDINARY);
        b_bus_pan_1185.setCapacity(52);
        b_bus_pan_1185.setStatus(BusStatus.ACTIVE);
        b_bus_pan_1185 = busRepository.save(b_bus_pan_1185);
        busMap.put("bus-pan-1185", b_bus_pan_1185);

        Bus b_bus_rew_1222 = new Bus();
        b_bus_rew_1222.setBusNumber("HR-36-G-1222");
        b_bus_rew_1222.setRegistrationNumber("HR-36-G-1222");
        b_bus_rew_1222.setBusType(BusType.ORDINARY);
        b_bus_rew_1222.setCapacity(52);
        b_bus_rew_1222.setStatus(BusStatus.ACTIVE);
        b_bus_rew_1222 = busRepository.save(b_bus_rew_1222);
        busMap.put("bus-rew-1222", b_bus_rew_1222);

        Bus b_bus_far_1259 = new Bus();
        b_bus_far_1259.setBusNumber("HR-29-G-1259");
        b_bus_far_1259.setRegistrationNumber("HR-29-G-1259");
        b_bus_far_1259.setBusType(BusType.ORDINARY);
        b_bus_far_1259.setCapacity(52);
        b_bus_far_1259.setStatus(BusStatus.ACTIVE);
        b_bus_far_1259 = busRepository.save(b_bus_far_1259);
        busMap.put("bus-far-1259", b_bus_far_1259);

        Bus b_bus_amb_1296 = new Bus();
        b_bus_amb_1296.setBusNumber("HR-01-G-1296");
        b_bus_amb_1296.setRegistrationNumber("HR-01-G-1296");
        b_bus_amb_1296.setBusType(BusType.ORDINARY);
        b_bus_amb_1296.setCapacity(52);
        b_bus_amb_1296.setStatus(BusStatus.ACTIVE);
        b_bus_amb_1296 = busRepository.save(b_bus_amb_1296);
        busMap.put("bus-amb-1296", b_bus_amb_1296);

        Bus b_bus_sir_1333 = new Bus();
        b_bus_sir_1333.setBusNumber("HR-24-G-1333");
        b_bus_sir_1333.setRegistrationNumber("HR-24-G-1333");
        b_bus_sir_1333.setBusType(BusType.ORDINARY);
        b_bus_sir_1333.setCapacity(52);
        b_bus_sir_1333.setStatus(BusStatus.ACTIVE);
        b_bus_sir_1333 = busRepository.save(b_bus_sir_1333);
        busMap.put("bus-sir-1333", b_bus_sir_1333);

        Bus b_bus_jin_1370 = new Bus();
        b_bus_jin_1370.setBusNumber("HR-31-G-1370");
        b_bus_jin_1370.setRegistrationNumber("HR-31-G-1370");
        b_bus_jin_1370.setBusType(BusType.ORDINARY);
        b_bus_jin_1370.setCapacity(52);
        b_bus_jin_1370.setStatus(BusStatus.ACTIVE);
        b_bus_jin_1370 = busRepository.save(b_bus_jin_1370);
        busMap.put("bus-jin-1370", b_bus_jin_1370);

        Bus b_bus_kai_1407 = new Bus();
        b_bus_kai_1407.setBusNumber("HR-08-G-1407");
        b_bus_kai_1407.setRegistrationNumber("HR-08-G-1407");
        b_bus_kai_1407.setBusType(BusType.ORDINARY);
        b_bus_kai_1407.setCapacity(52);
        b_bus_kai_1407.setStatus(BusStatus.ACTIVE);
        b_bus_kai_1407 = busRepository.save(b_bus_kai_1407);
        busMap.put("bus-kai-1407", b_bus_kai_1407);

        Bus b_bus_kar_1444 = new Bus();
        b_bus_kar_1444.setBusNumber("HR-05-G-1444");
        b_bus_kar_1444.setRegistrationNumber("HR-05-G-1444");
        b_bus_kar_1444.setBusType(BusType.ORDINARY);
        b_bus_kar_1444.setCapacity(52);
        b_bus_kar_1444.setStatus(BusStatus.ACTIVE);
        b_bus_kar_1444 = busRepository.save(b_bus_kar_1444);
        busMap.put("bus-kar-1444", b_bus_kar_1444);

        Bus b_bus_yam_1481 = new Bus();
        b_bus_yam_1481.setBusNumber("HR-02-G-1481");
        b_bus_yam_1481.setRegistrationNumber("HR-02-G-1481");
        b_bus_yam_1481.setBusType(BusType.ORDINARY);
        b_bus_yam_1481.setCapacity(52);
        b_bus_yam_1481.setStatus(BusStatus.ACTIVE);
        b_bus_yam_1481 = busRepository.save(b_bus_yam_1481);
        busMap.put("bus-yam-1481", b_bus_yam_1481);

        Bus b_bus_pan_1518 = new Bus();
        b_bus_pan_1518.setBusNumber("HR-03-G-1518");
        b_bus_pan_1518.setRegistrationNumber("HR-03-G-1518");
        b_bus_pan_1518.setBusType(BusType.ORDINARY);
        b_bus_pan_1518.setCapacity(52);
        b_bus_pan_1518.setStatus(BusStatus.ACTIVE);
        b_bus_pan_1518 = busRepository.save(b_bus_pan_1518);
        busMap.put("bus-pan-1518", b_bus_pan_1518);

        Bus b_bus_mah_1555 = new Bus();
        b_bus_mah_1555.setBusNumber("HR-34-G-1555");
        b_bus_mah_1555.setRegistrationNumber("HR-34-G-1555");
        b_bus_mah_1555.setBusType(BusType.ORDINARY);
        b_bus_mah_1555.setCapacity(52);
        b_bus_mah_1555.setStatus(BusStatus.ACTIVE);
        b_bus_mah_1555 = busRepository.save(b_bus_mah_1555);
        busMap.put("bus-mah-1555", b_bus_mah_1555);

        Bus b_bus_cha_1592 = new Bus();
        b_bus_cha_1592.setBusNumber("HR-19-G-1592");
        b_bus_cha_1592.setRegistrationNumber("HR-19-G-1592");
        b_bus_cha_1592.setBusType(BusType.ORDINARY);
        b_bus_cha_1592.setCapacity(52);
        b_bus_cha_1592.setStatus(BusStatus.ACTIVE);
        b_bus_cha_1592 = busRepository.save(b_bus_cha_1592);
        busMap.put("bus-cha-1592", b_bus_cha_1592);

        Bus b_bus_jha_1629 = new Bus();
        b_bus_jha_1629.setBusNumber("HR-14-G-1629");
        b_bus_jha_1629.setRegistrationNumber("HR-14-G-1629");
        b_bus_jha_1629.setBusType(BusType.ORDINARY);
        b_bus_jha_1629.setCapacity(52);
        b_bus_jha_1629.setStatus(BusStatus.ACTIVE);
        b_bus_jha_1629 = busRepository.save(b_bus_jha_1629);
        busMap.put("bus-jha-1629", b_bus_jha_1629);

        Bus b_bus_son_1666 = new Bus();
        b_bus_son_1666.setBusNumber("HR-10-G-1666");
        b_bus_son_1666.setRegistrationNumber("HR-10-G-1666");
        b_bus_son_1666.setBusType(BusType.ORDINARY);
        b_bus_son_1666.setCapacity(52);
        b_bus_son_1666.setStatus(BusStatus.ACTIVE);
        b_bus_son_1666 = busRepository.save(b_bus_son_1666);
        busMap.put("bus-son-1666", b_bus_son_1666);

        Bus b_bus_son_1703 = new Bus();
        b_bus_son_1703.setBusNumber("HR-10-G-1703");
        b_bus_son_1703.setRegistrationNumber("HR-10-G-1703");
        b_bus_son_1703.setBusType(BusType.ORDINARY);
        b_bus_son_1703.setCapacity(52);
        b_bus_son_1703.setStatus(BusStatus.ACTIVE);
        b_bus_son_1703 = busRepository.save(b_bus_son_1703);
        busMap.put("bus-son-1703", b_bus_son_1703);

        Bus b_bus_nuh_1740 = new Bus();
        b_bus_nuh_1740.setBusNumber("HR-27-G-1740");
        b_bus_nuh_1740.setRegistrationNumber("HR-27-G-1740");
        b_bus_nuh_1740.setBusType(BusType.ORDINARY);
        b_bus_nuh_1740.setCapacity(52);
        b_bus_nuh_1740.setStatus(BusStatus.ACTIVE);
        b_bus_nuh_1740 = busRepository.save(b_bus_nuh_1740);
        busMap.put("bus-nuh-1740", b_bus_nuh_1740);

        Bus b_bus_pal_1777 = new Bus();
        b_bus_pal_1777.setBusNumber("HR-30-G-1777");
        b_bus_pal_1777.setRegistrationNumber("HR-30-G-1777");
        b_bus_pal_1777.setBusType(BusType.ORDINARY);
        b_bus_pal_1777.setCapacity(52);
        b_bus_pal_1777.setStatus(BusStatus.ACTIVE);
        b_bus_pal_1777 = busRepository.save(b_bus_pal_1777);
        busMap.put("bus-pal-1777", b_bus_pal_1777);

        Bus b_bus_fat_1814 = new Bus();
        b_bus_fat_1814.setBusNumber("HR-22-G-1814");
        b_bus_fat_1814.setRegistrationNumber("HR-22-G-1814");
        b_bus_fat_1814.setBusType(BusType.ORDINARY);
        b_bus_fat_1814.setCapacity(52);
        b_bus_fat_1814.setStatus(BusStatus.ACTIVE);
        b_bus_fat_1814 = busRepository.save(b_bus_fat_1814);
        busMap.put("bus-fat-1814", b_bus_fat_1814);

        Bus b_bus_kur_1851 = new Bus();
        b_bus_kur_1851.setBusNumber("HR-07-G-1851");
        b_bus_kur_1851.setRegistrationNumber("HR-07-G-1851");
        b_bus_kur_1851.setBusType(BusType.ORDINARY);
        b_bus_kur_1851.setCapacity(52);
        b_bus_kur_1851.setStatus(BusStatus.ACTIVE);
        b_bus_kur_1851 = busRepository.save(b_bus_kur_1851);
        busMap.put("bus-kur-1851", b_bus_kur_1851);

        Bus b_bus_his_1888 = new Bus();
        b_bus_his_1888.setBusNumber("HR-20-G-1888");
        b_bus_his_1888.setRegistrationNumber("HR-20-G-1888");
        b_bus_his_1888.setBusType(BusType.ORDINARY);
        b_bus_his_1888.setCapacity(52);
        b_bus_his_1888.setStatus(BusStatus.ACTIVE);
        b_bus_his_1888 = busRepository.save(b_bus_his_1888);
        busMap.put("bus-his-1888", b_bus_his_1888);

        Bus b_bus_bhi_1925 = new Bus();
        b_bus_bhi_1925.setBusNumber("HR-16-G-1925");
        b_bus_bhi_1925.setRegistrationNumber("HR-16-G-1925");
        b_bus_bhi_1925.setBusType(BusType.ORDINARY);
        b_bus_bhi_1925.setCapacity(52);
        b_bus_bhi_1925.setStatus(BusStatus.ACTIVE);
        b_bus_bhi_1925 = busRepository.save(b_bus_bhi_1925);
        busMap.put("bus-bhi-1925", b_bus_bhi_1925);

        Bus b_bus_sir_1962 = new Bus();
        b_bus_sir_1962.setBusNumber("HR-24-G-1962");
        b_bus_sir_1962.setRegistrationNumber("HR-24-G-1962");
        b_bus_sir_1962.setBusType(BusType.ORDINARY);
        b_bus_sir_1962.setCapacity(52);
        b_bus_sir_1962.setStatus(BusStatus.ACTIVE);
        b_bus_sir_1962 = busRepository.save(b_bus_sir_1962);
        busMap.put("bus-sir-1962", b_bus_sir_1962);

        Bus b_bus_pal_1999 = new Bus();
        b_bus_pal_1999.setBusNumber("HR-30-G-1999");
        b_bus_pal_1999.setRegistrationNumber("HR-30-G-1999");
        b_bus_pal_1999.setBusType(BusType.ORDINARY);
        b_bus_pal_1999.setCapacity(52);
        b_bus_pal_1999.setStatus(BusStatus.ACTIVE);
        b_bus_pal_1999 = busRepository.save(b_bus_pal_1999);
        busMap.put("bus-pal-1999", b_bus_pal_1999);

        Bus b_bus_nuh_2036 = new Bus();
        b_bus_nuh_2036.setBusNumber("HR-27-G-2036");
        b_bus_nuh_2036.setRegistrationNumber("HR-27-G-2036");
        b_bus_nuh_2036.setBusType(BusType.ORDINARY);
        b_bus_nuh_2036.setCapacity(52);
        b_bus_nuh_2036.setStatus(BusStatus.ACTIVE);
        b_bus_nuh_2036 = busRepository.save(b_bus_nuh_2036);
        busMap.put("bus-nuh-2036", b_bus_nuh_2036);

        Bus b_bus_mah_2073 = new Bus();
        b_bus_mah_2073.setBusNumber("HR-34-G-2073");
        b_bus_mah_2073.setRegistrationNumber("HR-34-G-2073");
        b_bus_mah_2073.setBusType(BusType.ORDINARY);
        b_bus_mah_2073.setCapacity(52);
        b_bus_mah_2073.setStatus(BusStatus.ACTIVE);
        b_bus_mah_2073 = busRepository.save(b_bus_mah_2073);
        busMap.put("bus-mah-2073", b_bus_mah_2073);

        LocalDateTime now = LocalDateTime.now();

        Trip t_trip_gur_01 = new Trip();
        t_trip_gur_01.setTripNumber("TRIP-GUR-01");
        t_trip_gur_01.setBus(busMap.get("bus-gur-1000"));
        t_trip_gur_01.setRoute(routeMap.get("route-ggn-chd-01"));
        t_trip_gur_01.setDepartureTime(now.minusMinutes(30));
        t_trip_gur_01.setArrivalTime(now.plusMinutes(90));
        t_trip_gur_01.setStatus(TripStatus.RUNNING);
        tripRepository.save(t_trip_gur_01);

        Trip t_trip_roh_02 = new Trip();
        t_trip_roh_02.setTripNumber("TRIP-ROH-02");
        t_trip_roh_02.setBus(busMap.get("bus-roh-1037"));
        t_trip_roh_02.setRoute(routeMap.get("route-rtk-del-02"));
        t_trip_roh_02.setDepartureTime(now.minusMinutes(30));
        t_trip_roh_02.setArrivalTime(now.plusMinutes(90));
        t_trip_roh_02.setStatus(TripStatus.SCHEDULED);
        tripRepository.save(t_trip_roh_02);

        Trip t_trip_his_03 = new Trip();
        t_trip_his_03.setTripNumber("TRIP-HIS-03");
        t_trip_his_03.setBus(busMap.get("bus-his-1074"));
        t_trip_his_03.setRoute(routeMap.get("route-hsr-del-03"));
        t_trip_his_03.setDepartureTime(now.minusMinutes(30));
        t_trip_his_03.setArrivalTime(now.plusMinutes(90));
        t_trip_his_03.setStatus(TripStatus.RUNNING);
        tripRepository.save(t_trip_his_03);

        Trip t_trip_bhi_04 = new Trip();
        t_trip_bhi_04.setTripNumber("TRIP-BHI-04");
        t_trip_bhi_04.setBus(busMap.get("bus-bhi-1111"));
        t_trip_bhi_04.setRoute(routeMap.get("route-bhw-rtk-04"));
        t_trip_bhi_04.setDepartureTime(now.minusMinutes(30));
        t_trip_bhi_04.setArrivalTime(now.plusMinutes(90));
        t_trip_bhi_04.setStatus(TripStatus.SCHEDULED);
        tripRepository.save(t_trip_bhi_04);

        Trip t_trip_cha_05 = new Trip();
        t_trip_cha_05.setTripNumber("TRIP-CHA-05");
        t_trip_cha_05.setBus(busMap.get("bus-cha-1148"));
        t_trip_cha_05.setRoute(routeMap.get("route-cd-bhw-05"));
        t_trip_cha_05.setDepartureTime(now.minusMinutes(30));
        t_trip_cha_05.setArrivalTime(now.plusMinutes(90));
        t_trip_cha_05.setStatus(TripStatus.RUNNING);
        tripRepository.save(t_trip_cha_05);

        Trip t_trip_pan_06 = new Trip();
        t_trip_pan_06.setTripNumber("TRIP-PAN-06");
        t_trip_pan_06.setBus(busMap.get("bus-pan-1185"));
        t_trip_pan_06.setRoute(routeMap.get("route-pan-amb-06"));
        t_trip_pan_06.setDepartureTime(now.minusMinutes(30));
        t_trip_pan_06.setArrivalTime(now.plusMinutes(90));
        t_trip_pan_06.setStatus(TripStatus.SCHEDULED);
        tripRepository.save(t_trip_pan_06);

        Trip t_trip_rew_07 = new Trip();
        t_trip_rew_07.setTripNumber("TRIP-REW-07");
        t_trip_rew_07.setBus(busMap.get("bus-rew-1222"));
        t_trip_rew_07.setRoute(routeMap.get("route-rew-ggn-07"));
        t_trip_rew_07.setDepartureTime(now.minusMinutes(30));
        t_trip_rew_07.setArrivalTime(now.plusMinutes(90));
        t_trip_rew_07.setStatus(TripStatus.RUNNING);
        tripRepository.save(t_trip_rew_07);

        Trip t_trip_far_08 = new Trip();
        t_trip_far_08.setTripNumber("TRIP-FAR-08");
        t_trip_far_08.setBus(busMap.get("bus-far-1259"));
        t_trip_far_08.setRoute(routeMap.get("route-fbd-del-08"));
        t_trip_far_08.setDepartureTime(now.minusMinutes(30));
        t_trip_far_08.setArrivalTime(now.plusMinutes(90));
        t_trip_far_08.setStatus(TripStatus.SCHEDULED);
        tripRepository.save(t_trip_far_08);

        Trip t_trip_amb_09 = new Trip();
        t_trip_amb_09.setTripNumber("TRIP-AMB-09");
        t_trip_amb_09.setBus(busMap.get("bus-amb-1296"));
        t_trip_amb_09.setRoute(routeMap.get("route-amb-chd-09"));
        t_trip_amb_09.setDepartureTime(now.minusMinutes(30));
        t_trip_amb_09.setArrivalTime(now.plusMinutes(90));
        t_trip_amb_09.setStatus(TripStatus.RUNNING);
        tripRepository.save(t_trip_amb_09);

        Trip t_trip_sir_10 = new Trip();
        t_trip_sir_10.setTripNumber("TRIP-SIR-10");
        t_trip_sir_10.setBus(busMap.get("bus-sir-1333"));
        t_trip_sir_10.setRoute(routeMap.get("route-sir-hsr-10"));
        t_trip_sir_10.setDepartureTime(now.minusMinutes(30));
        t_trip_sir_10.setArrivalTime(now.plusMinutes(90));
        t_trip_sir_10.setStatus(TripStatus.SCHEDULED);
        tripRepository.save(t_trip_sir_10);

        Trip t_trip_jin_11 = new Trip();
        t_trip_jin_11.setTripNumber("TRIP-JIN-11");
        t_trip_jin_11.setBus(busMap.get("bus-jin-1370"));
        t_trip_jin_11.setRoute(routeMap.get("route-jnd-chd-11"));
        t_trip_jin_11.setDepartureTime(now.minusMinutes(30));
        t_trip_jin_11.setArrivalTime(now.plusMinutes(90));
        t_trip_jin_11.setStatus(TripStatus.RUNNING);
        tripRepository.save(t_trip_jin_11);

        Trip t_trip_kai_12 = new Trip();
        t_trip_kai_12.setTripNumber("TRIP-KAI-12");
        t_trip_kai_12.setBus(busMap.get("bus-kai-1407"));
        t_trip_kai_12.setRoute(routeMap.get("route-kth-krl-12"));
        t_trip_kai_12.setDepartureTime(now.minusMinutes(30));
        t_trip_kai_12.setArrivalTime(now.plusMinutes(90));
        t_trip_kai_12.setStatus(TripStatus.SCHEDULED);
        tripRepository.save(t_trip_kai_12);

        Trip t_trip_kar_13 = new Trip();
        t_trip_kar_13.setTripNumber("TRIP-KAR-13");
        t_trip_kar_13.setBus(busMap.get("bus-kar-1444"));
        t_trip_kar_13.setRoute(routeMap.get("route-krl-ymn-13"));
        t_trip_kar_13.setDepartureTime(now.minusMinutes(30));
        t_trip_kar_13.setArrivalTime(now.plusMinutes(90));
        t_trip_kar_13.setStatus(TripStatus.RUNNING);
        tripRepository.save(t_trip_kar_13);

        Trip t_trip_yam_14 = new Trip();
        t_trip_yam_14.setTripNumber("TRIP-YAM-14");
        t_trip_yam_14.setBus(busMap.get("bus-yam-1481"));
        t_trip_yam_14.setRoute(routeMap.get("route-ymn-chd-14"));
        t_trip_yam_14.setDepartureTime(now.minusMinutes(30));
        t_trip_yam_14.setArrivalTime(now.plusMinutes(90));
        t_trip_yam_14.setStatus(TripStatus.SCHEDULED);
        tripRepository.save(t_trip_yam_14);

        Trip t_trip_pan_15 = new Trip();
        t_trip_pan_15.setTripNumber("TRIP-PAN-15");
        t_trip_pan_15.setBus(busMap.get("bus-pan-1518"));
        t_trip_pan_15.setRoute(routeMap.get("route-pkl-amb-15"));
        t_trip_pan_15.setDepartureTime(now.minusMinutes(30));
        t_trip_pan_15.setArrivalTime(now.plusMinutes(90));
        t_trip_pan_15.setStatus(TripStatus.RUNNING);
        tripRepository.save(t_trip_pan_15);

        Trip t_trip_mah_16 = new Trip();
        t_trip_mah_16.setTripNumber("TRIP-MAH-16");
        t_trip_mah_16.setBus(busMap.get("bus-mah-1555"));
        t_trip_mah_16.setRoute(routeMap.get("route-mnd-del-16"));
        t_trip_mah_16.setDepartureTime(now.minusMinutes(30));
        t_trip_mah_16.setArrivalTime(now.plusMinutes(90));
        t_trip_mah_16.setStatus(TripStatus.SCHEDULED);
        tripRepository.save(t_trip_mah_16);

        Trip t_trip_cha_17 = new Trip();
        t_trip_cha_17.setTripNumber("TRIP-CHA-17");
        t_trip_cha_17.setBus(busMap.get("bus-cha-1592"));
        t_trip_cha_17.setRoute(routeMap.get("route-cd-rew-17"));
        t_trip_cha_17.setDepartureTime(now.minusMinutes(30));
        t_trip_cha_17.setArrivalTime(now.plusMinutes(90));
        t_trip_cha_17.setStatus(TripStatus.RUNNING);
        tripRepository.save(t_trip_cha_17);

        Trip t_trip_jha_18 = new Trip();
        t_trip_jha_18.setTripNumber("TRIP-JHA-18");
        t_trip_jha_18.setBus(busMap.get("bus-jha-1629"));
        t_trip_jha_18.setRoute(routeMap.get("route-jhj-del-18"));
        t_trip_jha_18.setDepartureTime(now.minusMinutes(30));
        t_trip_jha_18.setArrivalTime(now.plusMinutes(90));
        t_trip_jha_18.setStatus(TripStatus.SCHEDULED);
        tripRepository.save(t_trip_jha_18);

        Trip t_trip_son_19 = new Trip();
        t_trip_son_19.setTripNumber("TRIP-SON-19");
        t_trip_son_19.setBus(busMap.get("bus-son-1666"));
        t_trip_son_19.setRoute(routeMap.get("route-snp-del-19"));
        t_trip_son_19.setDepartureTime(now.minusMinutes(30));
        t_trip_son_19.setArrivalTime(now.plusMinutes(90));
        t_trip_son_19.setStatus(TripStatus.RUNNING);
        tripRepository.save(t_trip_son_19);

        Trip t_trip_son_20 = new Trip();
        t_trip_son_20.setTripNumber("TRIP-SON-20");
        t_trip_son_20.setBus(busMap.get("bus-son-1703"));
        t_trip_son_20.setRoute(routeMap.get("route-snp-goh-20"));
        t_trip_son_20.setDepartureTime(now.minusMinutes(30));
        t_trip_son_20.setArrivalTime(now.plusMinutes(90));
        t_trip_son_20.setStatus(TripStatus.SCHEDULED);
        tripRepository.save(t_trip_son_20);

        Trip t_trip_nuh_21 = new Trip();
        t_trip_nuh_21.setTripNumber("TRIP-NUH-21");
        t_trip_nuh_21.setBus(busMap.get("bus-nuh-1740"));
        t_trip_nuh_21.setRoute(routeMap.get("route-nuh-ggn-21"));
        t_trip_nuh_21.setDepartureTime(now.minusMinutes(30));
        t_trip_nuh_21.setArrivalTime(now.plusMinutes(90));
        t_trip_nuh_21.setStatus(TripStatus.RUNNING);
        tripRepository.save(t_trip_nuh_21);

        Trip t_trip_pal_22 = new Trip();
        t_trip_pal_22.setTripNumber("TRIP-PAL-22");
        t_trip_pal_22.setBus(busMap.get("bus-pal-1777"));
        t_trip_pal_22.setRoute(routeMap.get("route-pal-del-22"));
        t_trip_pal_22.setDepartureTime(now.minusMinutes(30));
        t_trip_pal_22.setArrivalTime(now.plusMinutes(90));
        t_trip_pal_22.setStatus(TripStatus.SCHEDULED);
        tripRepository.save(t_trip_pal_22);

        Trip t_trip_fat_23 = new Trip();
        t_trip_fat_23.setTripNumber("TRIP-FAT-23");
        t_trip_fat_23.setBus(busMap.get("bus-fat-1814"));
        t_trip_fat_23.setRoute(routeMap.get("route-fat-chd-23"));
        t_trip_fat_23.setDepartureTime(now.minusMinutes(30));
        t_trip_fat_23.setArrivalTime(now.plusMinutes(90));
        t_trip_fat_23.setStatus(TripStatus.RUNNING);
        tripRepository.save(t_trip_fat_23);

        Trip t_trip_kur_24 = new Trip();
        t_trip_kur_24.setTripNumber("TRIP-KUR-24");
        t_trip_kur_24.setBus(busMap.get("bus-kur-1851"));
        t_trip_kur_24.setRoute(routeMap.get("route-krk-del-24"));
        t_trip_kur_24.setDepartureTime(now.minusMinutes(30));
        t_trip_kur_24.setArrivalTime(now.plusMinutes(90));
        t_trip_kur_24.setStatus(TripStatus.SCHEDULED);
        tripRepository.save(t_trip_kur_24);

        Trip t_trip_his_25 = new Trip();
        t_trip_his_25.setTripNumber("TRIP-HIS-25");
        t_trip_his_25.setBus(busMap.get("bus-his-1888"));
        t_trip_his_25.setRoute(routeMap.get("route-hsr-chd-25"));
        t_trip_his_25.setDepartureTime(now.minusMinutes(30));
        t_trip_his_25.setArrivalTime(now.plusMinutes(90));
        t_trip_his_25.setStatus(TripStatus.RUNNING);
        tripRepository.save(t_trip_his_25);

        Trip t_trip_bhi_26 = new Trip();
        t_trip_bhi_26.setTripNumber("TRIP-BHI-26");
        t_trip_bhi_26.setBus(busMap.get("bus-bhi-1925"));
        t_trip_bhi_26.setRoute(routeMap.get("route-bhw-del-26"));
        t_trip_bhi_26.setDepartureTime(now.minusMinutes(30));
        t_trip_bhi_26.setArrivalTime(now.plusMinutes(90));
        t_trip_bhi_26.setStatus(TripStatus.SCHEDULED);
        tripRepository.save(t_trip_bhi_26);

        Trip t_trip_sir_27 = new Trip();
        t_trip_sir_27.setTripNumber("TRIP-SIR-27");
        t_trip_sir_27.setBus(busMap.get("bus-sir-1962"));
        t_trip_sir_27.setRoute(routeMap.get("route-sir-del-27"));
        t_trip_sir_27.setDepartureTime(now.minusMinutes(30));
        t_trip_sir_27.setArrivalTime(now.plusMinutes(90));
        t_trip_sir_27.setStatus(TripStatus.RUNNING);
        tripRepository.save(t_trip_sir_27);

        Trip t_trip_pal_28 = new Trip();
        t_trip_pal_28.setTripNumber("TRIP-PAL-28");
        t_trip_pal_28.setBus(busMap.get("bus-pal-1999"));
        t_trip_pal_28.setRoute(routeMap.get("route-pal-fbd-28"));
        t_trip_pal_28.setDepartureTime(now.minusMinutes(30));
        t_trip_pal_28.setArrivalTime(now.plusMinutes(90));
        t_trip_pal_28.setStatus(TripStatus.SCHEDULED);
        tripRepository.save(t_trip_pal_28);

        Trip t_trip_nuh_29 = new Trip();
        t_trip_nuh_29.setTripNumber("TRIP-NUH-29");
        t_trip_nuh_29.setBus(busMap.get("bus-nuh-2036"));
        t_trip_nuh_29.setRoute(routeMap.get("route-nuh-pal-29"));
        t_trip_nuh_29.setDepartureTime(now.minusMinutes(30));
        t_trip_nuh_29.setArrivalTime(now.plusMinutes(90));
        t_trip_nuh_29.setStatus(TripStatus.RUNNING);
        tripRepository.save(t_trip_nuh_29);

        Trip t_trip_mah_30 = new Trip();
        t_trip_mah_30.setTripNumber("TRIP-MAH-30");
        t_trip_mah_30.setBus(busMap.get("bus-mah-2073"));
        t_trip_mah_30.setRoute(routeMap.get("route-mnd-bhw-30"));
        t_trip_mah_30.setDepartureTime(now.minusMinutes(30));
        t_trip_mah_30.setArrivalTime(now.plusMinutes(90));
        t_trip_mah_30.setStatus(TripStatus.SCHEDULED);
        tripRepository.save(t_trip_mah_30);

        // Seed Bus Schedules with Timetables
        for (Map.Entry<String, Route> entry : routeMap.entrySet()) {
            Route r = entry.getValue();
            BusSchedule schedule1 = new BusSchedule();
            schedule1.setRoute(r);
            schedule1.setDepotName(r.getSource() + " Depot");
            schedule1.setDepartureTime(LocalTime.of(7, 30));
            schedule1.setArrivalTime(LocalTime.of(10, 30));
            schedule1.setOperatingDays("ALL_DAYS");
            schedule1.setEffectiveFrom(LocalDate.now().minusDays(30));
            schedule1.setEffectiveUntil(LocalDate.now().plusDays(365));
            schedule1.setVerified(true);
            schedule1.setRouteHeading(r.getSource() + " - " + r.getDestination());
            schedule1.setActualDestination(r.getDestination());
            schedule1.setVia("En-Route Major Hubs");
            schedule1.setServiceType("Ordinary");
            busScheduleRepository.save(schedule1);

            BusSchedule schedule2 = new BusSchedule();
            schedule2.setRoute(r);
            schedule2.setDepotName(r.getSource() + " Depot");
            schedule2.setDepartureTime(LocalTime.of(14, 0));
            schedule2.setArrivalTime(LocalTime.of(17, 0));
            schedule2.setOperatingDays("ALL_DAYS");
            schedule2.setEffectiveFrom(LocalDate.now().minusDays(30));
            schedule2.setEffectiveUntil(LocalDate.now().plusDays(365));
            schedule2.setVerified(true);
            schedule2.setRouteHeading(r.getSource() + " - " + r.getDestination());
            schedule2.setActualDestination(r.getDestination());
            schedule2.setVia("En-Route Major Hubs");
            schedule2.setServiceType("Express");
            busScheduleRepository.save(schedule2);
        }

        System.out.println("Initialized " + stopMap.size() + " stops, " + routeMap.size() + " routes, " + busMap.size() + " buses across Haryana!");
    }
}
