package com.hy.haeyoback.global.config;

import com.hy.haeyoback.safety.entity.*;
import com.hy.haeyoback.safety.repository.SafetyGuideRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;

@Configuration
@Profile({"local", "dev", "test"})
public class SafetyGuideDataInitializer {

    @Bean
    public CommandLineRunner initSafetyGuides(SafetyGuideRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                repository.save(new SafetyGuide(
                        "익수자 구조 및 응급처치",
                        "물에 빠진 사람을 발견했을 때 즉시 취해야 할 구조 행동과 응급처치 방법",
                        SafetySituation.DROWNING,
                        "응급처치",
                        List.of(
                                new SafetyStep(1, "119 신고", "즉시 119에 신고하고 정확한 위치를 알립니다."),
                                new SafetyStep(2, "구명 장비 투척", "구명환, 구명조끼, 로프 등을 익수자에게 던집니다."),
                                new SafetyStep(3, "안전 확보", "직접 물에 들어가는 것은 최후의 수단입니다. 자신의 안전을 먼저 확보하세요."),
                                new SafetyStep(4, "의식 확인", "구조 후 즉시 의식과 호흡을 확인합니다."),
                                new SafetyStep(5, "심폐소생술", "호흡이 없으면 즉시 CPR을 시작합니다. 가슴압박 30회, 인공호흡 2회를 반복합니다."),
                                new SafetyStep(6, "저체온증 대처", "젖은 옷을 벗기고 담요로 감싸 체온을 유지합니다.")
                        ),
                        List.of(
                                new SafetyWarning("위험", "훈련받지 않은 상태에서 직접 물에 뛰어들지 마세요. 2차 사고의 위험이 있습니다."),
                                new SafetyWarning("주의", "구조 후 반드시 병원 진료를 받아야 합니다. 이차 익수(dry drowning) 위험이 있습니다."),
                                new SafetyWarning("중요", "CPR은 119 구급대가 도착할 때까지 계속해야 합니다.")
                        ),
                        List.of(
                                new EmergencyContact("해양경찰", "122", "해상 구조 전문 기관"),
                                new EmergencyContact("소방서(119)", "119", "응급 구조 및 의료 지원"),
                                new EmergencyContact("해양안전종합상황실", "1588-3650", "해양 사고 신고 및 상담")
                        ),
                        1,
                        "https://example.com/thumbnails/drowning-rescue.jpg"
                ));

                repository.save(new SafetyGuide(
                        "선박 화재 발생 시 대응",
                        "선박에서 화재가 발생했을 때 승객과 승무원이 취해야 할 행동 지침",
                        SafetySituation.FIRE_ONBOARD,
                        "비상대응",
                        List.of(
                                new SafetyStep(1, "화재 발견 즉시 신고", "선내 비상벨을 울리고 선원에게 화재를 알립니다."),
                                new SafetyStep(2, "대피 경로 확인", "가장 가까운 비상구와 대피 경로를 파악합니다."),
                                new SafetyStep(3, "구명조끼 착용", "즉시 구명조끼를 착용합니다."),
                                new SafetyStep(4, "낮은 자세 유지", "연기를 피해 낮은 자세로 이동합니다."),
                                new SafetyStep(5, "집결지로 이동", "지정된 집결지로 신속히 이동합니다."),
                                new SafetyStep(6, "선원 지시 따르기", "선원의 지시에 따라 침착하게 행동합니다.")
                        ),
                        List.of(
                                new SafetyWarning("엄금", "엘리베이터는 절대 사용하지 마세요. 계단을 이용하세요."),
                                new SafetyWarning("위험", "개인 소지품을 챙기려 객실로 돌아가지 마세요."),
                                new SafetyWarning("주의", "패닉 상태가 되지 않도록 침착함을 유지하세요.")
                        ),
                        List.of(
                                new EmergencyContact("해양경찰", "122", "해상 구조"),
                                new EmergencyContact("소방서", "119", "화재 진압 지원")
                        ),
                        1,
                        "https://example.com/thumbnails/ship-fire.jpg"
                ));

                repository.save(new SafetyGuide(
                        "기상 악화 시 안전 수칙",
                        "해상에서 갑작스러운 기상 악화 시 안전을 지키는 방법",
                        SafetySituation.WEATHER_WARNING,
                        "기상대응",
                        List.of(
                                new SafetyStep(1, "기상 정보 확인", "기상청 해양기상 예보를 실시간으로 확인합니다."),
                                new SafetyStep(2, "즉시 귀항 판단", "풍속 15m/s 이상, 파고 2m 이상이면 즉시 귀항합니다."),
                                new SafetyStep(3, "구명조끼 착용", "모든 승객은 구명조끼를 착용합니다."),
                                new SafetyStep(4, "선내 대피", "갑판에서 내려와 선실 안쪽으로 대피합니다."),
                                new SafetyStep(5, "고정물 잡기", "흔들림에 대비해 고정된 물체를 잡습니다."),
                                new SafetyStep(6, "통신 유지", "해양경찰 및 해양안전센터와 통신을 유지합니다.")
                        ),
                        List.of(
                                new SafetyWarning("위험", "높은 파도가 칠 때 갑판에 나가지 마세요."),
                                new SafetyWarning("주의", "멀미가 심하면 선원에게 즉시 알리세요."),
                                new SafetyWarning("중요", "기상특보(주의보, 경보) 발령 시 출항을 취소하세요.")
                        ),
                        List.of(
                                new EmergencyContact("해양경찰", "122", "해상 긴급 구조"),
                                new EmergencyContact("기상청", "131", "기상 정보 문의"),
                                new EmergencyContact("해양안전종합상황실", "1588-3650", "해양 안전 상담")
                        ),
                        2,
                        "https://example.com/thumbnails/bad-weather.jpg"
                ));

                repository.save(new SafetyGuide(
                        "선박 충돌 사고 대응",
                        "선박 간 충돌 또는 암초 충돌 시 승객 행동 요령",
                        SafetySituation.COLLISION,
                        "비상대응",
                        List.of(
                                new SafetyStep(1, "충격 대비 자세", "충돌 직전 낮은 자세로 고정물을 잡습니다."),
                                new SafetyStep(2, "부상 확인", "자신과 주변 사람의 부상 여부를 확인합니다."),
                                new SafetyStep(3, "구명조끼 착용", "즉시 구명조끼를 착용합니다."),
                                new SafetyStep(4, "침수 확인", "선체 침수 여부를 확인합니다."),
                                new SafetyStep(5, "집결지 이동", "선원 지시에 따라 집결지로 이동합니다."),
                                new SafetyStep(6, "대기 및 대피 준비", "구명정 탑승 또는 구조를 기다립니다.")
                        ),
                        List.of(
                                new SafetyWarning("위험", "충돌 후 갑작스러운 침수가 발생할 수 있습니다."),
                                new SafetyWarning("주의", "유리 파편 등 2차 부상에 주의하세요."),
                                new SafetyWarning("중요", "선원의 지시 없이 임의로 대피하지 마세요.")
                        ),
                        List.of(
                                new EmergencyContact("해양경찰", "122", "긴급 구조"),
                                new EmergencyContact("소방서(119)", "119", "응급 의료")
                        ),
                        1,
                        "https://example.com/thumbnails/collision.jpg"
                ));

                repository.save(new SafetyGuide(
                        "선박 탑승 전 안전 확인 사항",
                        "안전한 해양 활동을 위한 탑승 전 필수 점검 항목",
                        SafetySituation.GENERAL_SAFETY,
                        "일반안전",
                        List.of(
                                new SafetyStep(1, "기상 확인", "출항 전 해양기상 예보를 확인합니다."),
                                new SafetyStep(2, "구명조끼 위치 파악", "구명조끼 보관 위치와 착용법을 확인합니다."),
                                new SafetyStep(3, "비상구 확인", "비상구와 대피 경로를 미리 파악합니다."),
                                new SafetyStep(4, "안전 수칙 숙지", "선박 안전 수칙을 읽고 숙지합니다."),
                                new SafetyStep(5, "개인 안전 점검", "음주 여부, 건강 상태를 확인합니다."),
                                new SafetyStep(6, "비상 연락망 저장", "해양경찰(122), 119 번호를 휴대폰에 저장합니다.")
                        ),
                        List.of(
                                new SafetyWarning("주의", "과도한 음주 상태에서 탑승하지 마세요."),
                                new SafetyWarning("중요", "어린이는 반드시 보호자와 동승해야 합니다."),
                                new SafetyWarning("권장", "출항 전 가족에게 항로와 귀항 예정 시간을 알립니다.")
                        ),
                        List.of(
                                new EmergencyContact("해양경찰", "122", "해상 안전 신고"),
                                new EmergencyContact("해양안전종합상황실", "1588-3650", "해양 안전 상담")
                        ),
                        3,
                        "https://example.com/thumbnails/boarding-safety.jpg"
                ));
            }
        };
    }
}
