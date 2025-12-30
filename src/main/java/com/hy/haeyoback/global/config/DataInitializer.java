package com.hy.haeyoback.global.config;

import com.hy.haeyoback.domain.learning.entity.LearningContent;
import com.hy.haeyoback.domain.learning.repository.LearningContentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initLearningContents(LearningContentRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                repository.save(new LearningContent(
                        "구명조끼 올바른 착용법",
                        "비상 상황에서 생명을 구하는 구명조끼의 올바른 착용 방법을 배웁니다.",
                        "안전장비",
                        "https://example.com/thumbnails/lifejacket.jpg",
                        "# 구명조끼 올바른 착용법\n\n구명조끼는 해상 사고 시 생명을 구하는 가장 중요한 안전 장비입니다.\n\n## 착용 전 확인사항\n- 구명조끼의 손상 여부 확인\n- 공기 주입 장치 작동 확인\n- 호루라기 및 구조등 부착 확인\n\n## 올바른 착용 방법\n1. 머리 위로 구명조끼를 씌웁니다\n2. 가슴 부분의 벨트를 단단히 조입니다\n3. 배 부분의 벨트를 조입니다\n4. 다리 사이의 벨트를 연결합니다\n\n## 주의사항\n- 느슨하게 착용하면 물에 빠졌을 때 벗겨질 수 있습니다\n- 어린이는 반드시 어린이용 구명조끼를 착용해야 합니다\n- 긴급 상황에서는 자동 팽창식 구명조끼가 자동으로 부풀어 오릅니다"
                ));

                repository.save(new LearningContent(
                        "해상 조난 신호 방법",
                        "긴급 상황에서 구조 요청을 위한 다양한 신호 방법을 학습합니다.",
                        "비상대응",
                        "https://example.com/thumbnails/distress-signal.jpg",
                        "# 해상 조난 신호 방법\n\n해상에서 조난당했을 때 신속한 구조를 위해 올바른 신호 방법을 알아야 합니다.\n\n## 국제 조난 신호\n1. **SOS**: 모스 부호로 3짧-3길-3짧 (... --- ...)\n2. **메이데이**: 무선 통신으로 \"MAYDAY\" 3회 반복\n3. **팬팬**: 긴급하지만 생명 위협은 없을 때 \"PAN-PAN\" 3회\n\n## 시각적 신호\n- **연막**: 주간에 주황색 연막 사용\n- **조명탄**: 야간에 빨간색 조명탄 발사\n- **거울 반사**: 햇빛을 반사하여 신호\n- **팔 흔들기**: 양팔을 천천히 위아래로 흔들기\n\n## 음향 신호\n- 호루라기를 짧게 3번 불기\n- 1분 간격으로 반복\n\n## 무전 신호\n- VHF 채널 16번 사용\n- 위치, 인원, 상황 명확히 전달"
                ));

                repository.save(new LearningContent(
                        "해양 기상 이해하기",
                        "안전한 해양 활동을 위한 기상 정보 해석 방법을 배웁니다.",
                        "기상정보",
                        "https://example.com/thumbnails/weather.jpg",
                        "# 해양 기상 이해하기\n\n바다의 날씨는 육지보다 변화가 빠르고 예측이 어렵습니다.\n\n## 파도 정보\n- **파고**: 파도의 높이 (미터 단위)\n  - 0.5m 이하: 잔잔\n  - 1-2m: 약간 높음\n  - 2m 이상: 높음, 주의 필요\n\n## 풍속과 풍향\n- **풍속**: 바람의 세기 (m/s 또는 knot)\n  - 10m/s 이하: 약함\n  - 10-15m/s: 보통\n  - 15m/s 이상: 강함, 출항 자제\n\n## 기상특보\n- **주의보**: 해상 활동에 영향을 줄 수 있는 기상 현상\n- **경보**: 중대한 위험이 예상되는 경우\n\n## 구름과 하늘 관찰\n- 검은 구름이 빠르게 다가오면 폭풍 전조\n- 갑작스런 기온 변화는 기상 악화 신호\n- 파도가 높아지고 물결이 거칠어지면 즉시 육지로\n\n## 기상 정보 확인\n- 기상청 해양기상 예보 확인\n- 해양 앱 활용\n- 출항 전 반드시 기상 정보 확인"
                ));

                repository.save(new LearningContent(
                        "선박 안전 수칙",
                        "선박 탑승 시 반드시 지켜야 할 안전 수칙을 학습합니다.",
                        "선박안전",
                        "https://example.com/thumbnails/boat-safety.jpg",
                        "# 선박 안전 수칙\n\n선박 사고의 대부분은 기본 안전 수칙을 지키지 않아 발생합니다.\n\n## 탑승 전 확인사항\n- 날씨 확인\n- 구명조끼 위치 확인\n- 비상구 위치 파악\n- 구명보트 위치 확인\n\n## 탑승 중 안전수칙\n1. **구명조끼 착용**: 항상 착용 또는 가까이 보관\n2. **금연**: 지정된 장소에서만 흡연\n3. **과음 금지**: 음주는 판단력을 흐리게 합니다\n4. **난간 주의**: 난간에 기대거나 넘어가지 않기\n5. **어린이 감독**: 어린이는 항상 보호자와 함께\n\n## 비상 상황 대처\n- 선원의 지시에 따라 침착하게 행동\n- 구명조끼를 착용하고 비상구로 이동\n- 패닉 상태 금지, 질서 유지\n\n## 낙수 사고 예방\n- 갑판에서는 안전 난간 안쪽으로만 이동\n- 파도가 칠 때는 난간 근처에 가지 않기\n- 미끄러운 곳 주의\n- 술에 취한 상태로 갑판 출입 금지"
                ));

                repository.save(new LearningContent(
                        "해양 응급처치 기본",
                        "해양 사고 시 응급 상황에 대처하는 기본 응급처치법을 배웁니다.",
                        "응급처치",
                        "https://example.com/thumbnails/first-aid.jpg",
                        "# 해양 응급처치 기본\n\n바다에서는 즉각적인 의료 지원이 어려울 수 있어 기본 응급처치가 중요합니다.\n\n## 익수자 구조\n1. **구조 순서**\n   - 119에 신고\n   - 구명 장비 던지기 (구명환, 로프)\n   - 직접 물에 들어가는 것은 최후의 수단\n\n2. **구조 후 응급처치**\n   - 의식 확인\n   - 호흡 확인\n   - 필요시 심폐소생술 (CPR)\n\n## 심폐소생술 (CPR)\n1. 가슴 압박 30회 (1분당 100-120회)\n2. 인공호흡 2회\n3. 반복 실시\n\n## 저체온증 대처\n- 젖은 옷 벗기기\n- 담요나 마른 옷으로 감싸기\n- 따뜻한 음료 제공 (의식이 있을 때만)\n- 급격한 온도 변화 피하기\n\n## 해파리 쏘임\n- 식초로 환부 세척\n- 촉수 제거 (맨손 사용 금지)\n- 얼음찜질\n- 통증이 심하면 병원 방문\n\n## 출혈 처치\n- 깨끗한 천으로 상처 압박\n- 상처 부위를 심장보다 높게\n- 지혈대는 최후의 수단\n\n## 응급 약품 준비\n- 소독약, 붕대, 밴드\n- 진통제, 소화제\n- 멀미약\n- 화상 연고"
                ));
            }
        };
    }
}
