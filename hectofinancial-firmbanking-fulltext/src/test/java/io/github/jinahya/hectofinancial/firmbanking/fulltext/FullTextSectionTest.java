package io.github.jinahya.hectofinancial.firmbanking.fulltext;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class FullTextSectionTest {

    public static FullTextSection newInstanceForCommonSection() {
        final var instance = FullTextSection.newInstanceForCommonSection();
        final var buffer = ByteBuffer.allocate(100);
        final var number = ThreadLocalRandom.current().nextInt(950000) + 1;
        final var date = LocalDate.now();
        final var time = LocalTime.now().withNano(0);
        return instance
                .value(0x1, buffer, "SETTLEBNK")   //  1 식별코드
                .value(0x2, buffer, "jt3oc;tsmv")  //  2 업체번호
                .value(0x3, buffer, "002")         //  3 은행코드
                .value(0x4, buffer, "1000")        //  4 전문구분코드
                .value(0x5, buffer, "100")         //  5 업무구분코드
                .value(0x6, buffer, 1)             //  6 송신회수
                .int__(0x7, buffer, number)        //  7 전문번호
                .date_(0x8, buffer, date)          //  8 전송일자
                .time_(0x9, buffer, time)          //  8 전송시간
                .value(0xa, buffer, "")            // 10 응답코드
                .value(0xb, buffer, "j7;djeajex1") // 11 예비영역1 X(15)
                .value(0xc, buffer, "j7;djeajex2") // 12 예비영역2 X(11)
                .value(0xd, buffer, "j7;djeajex3") // 13 예비영역3 X(18)
                ;
    }

    @Nested
    class CommonSectionTest {

        @Test
        void __offset() {
            final var instance = FullTextSection.newInstanceForCommonSection();
            assertThat(instance).isNotNull().satisfies(s -> {
                final var segments = s.getSegments();
                assertThat(segments).isNotEmpty();
                assertThat(segments.stream().mapToInt(FullTextSegment::getOffset))
                        .hasSameElementsAs(List.of(0, 9, 21, 24, 28, 31, 32, 38, 46, 52, 56, 71, 82));
            });
        }

        @Test
        void __() {
            final var instance = FullTextSection.newInstanceForCommonSection();
            final var buffer = ByteBuffer.allocate(100);
            final var number = ThreadLocalRandom.current().nextInt(950000) + 1;
            final var date = LocalDate.now();
            final var time = LocalTime.now().withNano(0);
            instance
                    .value(0x1, buffer, "SETTLEBNK")   //  1 식별코드
                    .value(0x2, buffer, "jt3oc;tsmv")  //  2 업체번호
                    .value(0x3, buffer, "002")         //  3 은행코드
                    .value(0x4, buffer, "1000")        //  4 전문구분코드
                    .value(0x5, buffer, "100")         //  5 업무구분코드
                    .value(0x6, buffer, 1)             //  6 송신회수
                    .int__(0x7, buffer, number)        //  7 전문번호
                    .date_(0x8, buffer, date)          //  8 전송일자
                    .time_(0x9, buffer, time)          //  8 전송시간
                    .value(0xa, buffer, "")            // 10 응답코드
                    .value(0xb, buffer, "j7;djeajex1") // 11 예비영역1 X(15)
                    .value(0xc, buffer, "j7;djeajex2") // 12 예비영역2 X(11)
                    .value(0xd, buffer, "j7;djeajex3") // 13 예비영역3 X(18)
            ;
            final var encoded = FullTextSegmentCodecOfX.CHARSET.decode(buffer).toString();
            log.debug("encoded: [{}]", encoded);
            assertThat(instance.int__(7, buffer)).isEqualTo(number);
            assertThat(instance.date_(8, buffer)).isEqualTo(date);
            assertThat(instance.time_(9, buffer)).isEqualTo(time);
        }
    }
}