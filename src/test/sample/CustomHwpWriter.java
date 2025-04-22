package sample;

import kr.dogfoot.hwplib.object.HWPFile;
import kr.dogfoot.hwplib.object.bodytext.Section;
import kr.dogfoot.hwplib.object.bodytext.paragraph.Paragraph;
import kr.dogfoot.hwplib.object.bodytext.paragraph.charshape.ParaCharShape;
import kr.dogfoot.hwplib.object.bodytext.paragraph.header.ParaHeader;
import kr.dogfoot.hwplib.object.bodytext.paragraph.lineseg.LineSegItem;
import kr.dogfoot.hwplib.object.bodytext.paragraph.lineseg.ParaLineSeg;
import kr.dogfoot.hwplib.object.bodytext.paragraph.text.ParaText;
import kr.dogfoot.hwplib.object.docinfo.CharShape;
import kr.dogfoot.hwplib.object.docinfo.FaceName;
import kr.dogfoot.hwplib.object.docinfo.charshape.*;
import kr.dogfoot.hwplib.writer.HWPWriter;

import java.io.File;
import java.io.UnsupportedEncodingException;

/**
 * 커스텀 HWP 문서 생성기 샘플
 * 
 * @author Claude
 */
public class CustomHwpWriter {
    private HWPFile hwpFile;
    private int charShapeIndexForNormal;
    private int charShapeIndexForBold;
    private int charShapeIndexForTitle;
    private int faceNameIndexForBatang;
    private int faceNameIndexForGulim;

    /**
     * 임시 문서를 생성하는 메인 메소드
     * 
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        CustomHwpWriter customWriter = new CustomHwpWriter();
        String filename = "sample_test.hwp";
        customWriter.createSampleDocument(filename);
        System.out.println(filename + " 파일이 생성되었습니다.");
    }

    /**
     * 생성자
     */
    public CustomHwpWriter() {
        hwpFile = new HWPFile();
    }

    /**
     * 샘플 문서를 생성하는 메소드
     * 
     * @param filename 생성할 파일명
     * @throws Exception
     */
    public void createSampleDocument(String filename) throws Exception {
        // 폰트 및 글자 모양 설정
        faceNameIndexForBatang = createFaceNameForFont("바탕");
        faceNameIndexForGulim = createFaceNameForFont("굴림");
        charShapeIndexForNormal = createCharShape(faceNameIndexForBatang, 10, false, false);
        charShapeIndexForBold = createCharShape(faceNameIndexForBatang, 10, true, false);
        charShapeIndexForTitle = createCharShape(faceNameIndexForGulim, 16, true, false);
        
        // 새로운 섹션 생성
        Section section = hwpFile.getBodyText().addNewSection();
        
        // 제목 문단 추가
        Paragraph titleParagraph = section.addNewParagraph();
        setParaHeader(titleParagraph, true);
        setParaText(titleParagraph, "HWPLib 테스트 문서");
        // 제목에 특별한 글자 모양 적용
        setParaCharShape(titleParagraph, 0, charShapeIndexForTitle);
        setParaLineSeg(titleParagraph);
        
        // 본문 문단 추가
        Paragraph paragraph1 = section.addNewParagraph();
        setParaHeader(paragraph1, false);
        setParaText(paragraph1, "이 문서는 HWPLib을 사용하여 프로그래밍 방식으로 생성되었습니다.");
        setParaCharShape(paragraph1, 0, charShapeIndexForNormal);
        setParaLineSeg(paragraph1);
        
        Paragraph paragraph2 = section.addNewParagraph();
        setParaHeader(paragraph2, false);
        setParaText(paragraph2, "다양한 글자 모양과 문단 스타일을 적용할 수 있습니다. 여기서부터는 ");
        appendParaText(paragraph2, "굵은 글씨로 강조");
        appendParaText(paragraph2, "하는 방법을 보여줍니다.");
        
        // 부분적으로 글자 모양 적용
        setParaCharShape(paragraph2, 0, charShapeIndexForNormal);  // 기본 텍스트
        setParaCharShape(paragraph2, 29, charShapeIndexForBold);   // "굵은 글씨로 강조" 부분
        setParaCharShape(paragraph2, 38, charShapeIndexForNormal); // 나머지 부분
        setParaLineSeg(paragraph2);
        
        Paragraph paragraph3 = section.addNewParagraph();
        setParaHeader(paragraph3, true);
        setParaText(paragraph3, "이 예제는 간단한 텍스트만 포함하고 있지만, 다양한 서식과 객체를 추가할 수 있습니다.");
        setParaCharShape(paragraph3, 0, charShapeIndexForNormal);
        setParaLineSeg(paragraph3);
        
        // 파일로 저장
        String path = new File(filename).getAbsolutePath();
        HWPWriter.toFile(hwpFile, path);
    }
    
    /**
     * 폰트를 위한 FaceName 객체를 생성
     * 
     * @param fontName 폰트 이름
     * @return FaceName 인덱스
     */
    private int createFaceNameForFont(String fontName) {
        FaceName fn;

        // 한글 부분을 위한 FaceName 객체를 생성
        fn = hwpFile.getDocInfo().addNewHangulFaceName();
        setFaceNameForFont(fn, fontName);

        // 영어 부분을 위한 FaceName 객체를 생성
        fn = hwpFile.getDocInfo().addNewEnglishFaceName();
        setFaceNameForFont(fn, fontName);

        // 한자 부분을 위한 FaceName 객체를 생성
        fn = hwpFile.getDocInfo().addNewHanjaFaceName();
        setFaceNameForFont(fn, fontName);

        // 일본어 부분을 위한 FaceName 객체를 생성
        fn = hwpFile.getDocInfo().addNewJapaneseFaceName();
        setFaceNameForFont(fn, fontName);

        // 기타 문자 부분을 위한 FaceName 객체를 생성
        fn = hwpFile.getDocInfo().addNewEtcFaceName();
        setFaceNameForFont(fn, fontName);

        // 기호 문자 부분을 위한 FaceName 객체를 생성
        fn = hwpFile.getDocInfo().addNewSymbolFaceName();
        setFaceNameForFont(fn, fontName);

        // 사용자 정의 문자 부분을 위한 FaceName 객체를 생성
        fn = hwpFile.getDocInfo().addNewUserFaceName();
        setFaceNameForFont(fn, fontName);

        return hwpFile.getDocInfo().getHangulFaceNameList().size() - 1;
    }

    /**
     * FaceName 객체를 설정
     * 
     * @param fn 설정할 FaceName 객체
     * @param fontName 폰트 이름
     */
    private void setFaceNameForFont(FaceName fn, String fontName) {
        fn.getProperty().setHasBaseFont(false);
        fn.getProperty().setHasFontInfo(false);
        fn.getProperty().setHasSubstituteFont(false);
        fn.setName(fontName);
    }

    /**
     * 글자 모양 생성
     * 
     * @param faceNameIndex 폰트 인덱스
     * @param fontSize 폰트 크기(pt)
     * @param bold 굵은 글씨 여부
     * @param italic 이탤릭체 여부
     * @return 생성된 글자 모양 인덱스
     */
    private int createCharShape(int faceNameIndex, int fontSize, boolean bold, boolean italic) {
        CharShape cs = hwpFile.getDocInfo().addNewCharShape();
        
        // 폰트 설정
        cs.getFaceNameIds().setForAll(faceNameIndex);

        cs.getRatios().setForAll((short) 100);
        cs.getCharSpaces().setForAll((byte) 0);
        cs.getRelativeSizes().setForAll((short) 100);
        cs.getCharOffsets().setForAll((byte) 0);
        cs.setBaseSize(ptToBaseSize(fontSize));

        // 글자 속성 설정
        cs.getProperty().setItalic(italic);
        cs.getProperty().setBold(bold);
        cs.getProperty().setUnderLineSort(UnderLineSort.None);
        cs.getProperty().setUnderLineShape(BorderType2.Solid);
        cs.getProperty().setOutterLineSort(OutterLineSort.None);
        cs.getProperty().setShadowSort(ShadowSort.None);
        cs.getProperty().setEmboss(false);
        cs.getProperty().setEngrave(false);
        cs.getProperty().setSuperScript(false);
        cs.getProperty().setSubScript(false);
        cs.getProperty().setStrikeLine(false);
        cs.getProperty().setEmphasisSort(EmphasisSort.None);
        cs.getProperty().setUsingSpaceAppropriateForFont(false);
        cs.getProperty().setStrikeLineShape(BorderType2.Solid);
        cs.getProperty().setKerning(false);

        // 그림자 및 색상 설정
        cs.setShadowGap1((byte) 0);
        cs.setShadowGap2((byte) 0);
        cs.getCharColor().setValue(0x00000000);  // 검은색
        cs.getUnderLineColor().setValue(0x00000000);
        cs.getShadeColor().setValue(-1);
        cs.getShadowColor().setValue(0x00b2b2b2);
        cs.setBorderFillId(0);

        return hwpFile.getDocInfo().getCharShapeList().size() - 1;
    }

    /**
     * pt 단위를 HWP 내부 단위로 변환
     * 
     * @param pt 포인트 크기
     * @return HWP 내부 단위 크기
     */
    private int ptToBaseSize(int pt) {
        return pt * 100;
    }

    /**
     * 문단 헤더 설정
     * 
     * @param p 문단 객체
     * @param isLastInList 마지막 문단 여부
     */
    private void setParaHeader(Paragraph p, boolean isLastInList) {
        ParaHeader ph = p.getHeader();
        ph.setLastInList(isLastInList);
        ph.setParaShapeId(1);  // 기본 문단 모양 사용
        ph.setStyleId((short) 1);  // 기본 스타일 사용
        ph.getDivideSort().setDivideSection(false);
        ph.getDivideSort().setDivideMultiColumn(false);
        ph.getDivideSort().setDividePage(false);
        ph.getDivideSort().setDivideColumn(false);
        ph.setCharShapeCount(1);
        ph.setRangeTagCount(0);
        ph.setLineAlignCount(1);
        ph.setInstanceID(0);
        ph.setIsMergedByTrack(0);
    }

    /**
     * 문단에 텍스트 설정
     * 
     * @param paragraph 문단 객체
     * @param text 추가할 텍스트
     */
    private void setParaText(Paragraph paragraph, String text) {
        paragraph.createText();
        ParaText pt = paragraph.getText();
        try {
            pt.addString(text);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 기존 문단에 텍스트 추가
     * 
     * @param paragraph 문단 객체
     * @param text 추가할 텍스트
     */
    private void appendParaText(Paragraph paragraph, String text) {
        ParaText pt = paragraph.getText();
        try {
            pt.addString(text);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 문단 글자 모양 설정
     * 
     * @param paragraph 문단 객체
     * @param startPos 시작 위치
     * @param charShapeIndex 글자 모양 인덱스
     */
    private void setParaCharShape(Paragraph paragraph, int startPos, int charShapeIndex) {
        if (paragraph.getCharShape() == null) {
            paragraph.createCharShape();
        }
        ParaCharShape pcs = paragraph.getCharShape();
        pcs.addParaCharShape(startPos, charShapeIndex);
    }

    /**
     * 문단 라인 레이아웃 설정
     * 
     * @param paragraph 문단 객체
     */
    private void setParaLineSeg(Paragraph paragraph) {
        paragraph.createLineSeg();

        ParaLineSeg pls = paragraph.getLineSeg();
        LineSegItem lsi = pls.addNewLineSegItem();

        lsi.setTextStartPosition(0);
        lsi.setLineVerticalPosition(0);
        lsi.setLineHeight(ptToLineHeight(10.0));
        lsi.setTextPartHeight(ptToLineHeight(10.0));
        lsi.setDistanceBaseLineToLineVerticalPosition(ptToLineHeight(10.0 * 0.85));
        lsi.setLineSpace(ptToLineHeight(3.0));
        lsi.setStartPositionFromColumn(0);
        lsi.setSegmentWidth(mmToHwp(150.0));
        lsi.getTag().setFirstSegmentAtLine(true);
        lsi.getTag().setLastSegmentAtLine(true);
    }

    /**
     * pt 단위를 라인 높이 단위로 변환
     * 
     * @param pt 포인트 크기
     * @return 라인 높이 단위
     */
    private int ptToLineHeight(double pt) {
        return (int) (pt * 100.0f);
    }
    
    /**
     * mm 단위를 HWP 내부 단위로 변환 (1 mm = 72000 / 254 hwp unit)
     * 
     * @param mm 밀리미터 단위
     * @return HWP 내부 단위
     */
    private int mmToHwp(double mm) {
        return (int) (mm * 72000.0 / 254.0);
    }
} 