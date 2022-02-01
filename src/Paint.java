import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
 
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
 
public class Paint extends JFrame {
    
    JPanel gui, paint; //  위쪽은 gui, 아래쪽은 paint 로 이루어진 그림판
    
    JButton pencil, eraser, color, alleraser; // 연필, 지우개, 색, 모두지우기 선택
   
    JLabel thicknessInfo; // 도구굵기 라벨
    
    JTextField thicknessControl; // 도구 굵기 입력
    
    Color selectedColor; 
    // 현 변수에 컬러가 저장되어 추후에 펜색상을 정해주는 변수의 매개변수로 사용된다.
    
    Graphics graphics; // Graphics2D 클래스의 사용을 위해 선언
    Graphics2D g;// Graphics2D는 쉽게 말해 기존 graphics의 상위버전
    			 // Graphics2D 부분은 인터넷을 참조하다가  발견되서 사용해봣습니다.
    
    int thickness = 5; // 현 변수는 그려지는 선의 굴기를 변경할때 변경값이 저장되는 변수
    int startX; // 마우스클릭시작의 X좌표값이 저장될 변수
    int startY; // 마우스클릭시작의 Y좌표값이 저장될 변수
    int endX; // 마우스클릭종료의 X좌표값이 저장될 변수
    int endY; // 마우스클릭종료의 Y좌표값이 저장될 변수
    
    boolean tf = false; 
    /* 변 boolean 변수는 처음에 연필로 그리고 지우개로 지운다음 다시 연필로 그릴때
     * 기본색인 검은색으로 구분시키고 만약 프로그램 시작시 색선택후 그 선택된 색이
     * 지우개로 지우고 다시 연필로 그릴때 미리 정해진 색상으로 구분하는 변수인데..
     * 뭐 그리 중요한 변수는 아니다..
     */
    
    public Paint() { 
        setLayout(null); // 레이아웃을 없애 원하는대로 배치
        setTitle("그림판"); // 제목 지정
        setSize(900,750); // 사이즈 지정
        setLocationRelativeTo(null); // 프로그램 실행시 화면 중앙에 출력
        setDefaultCloseOperation(EXIT_ON_CLOSE); 
        // 프레임 우측상단에 X버튼을 눌렀을떄의 기능 정의
        
        gui = new JPanel(); // 프레임 상단에 버튼, 텍스트필드, 라벨등이 UI가 들어갈 패널
        gui.setBackground(Color.GRAY); // 패널의 배경색을 회색으로 지정
        gui.setLayout(null); 
        // gui_panel의 레이아웃을 null지정하여 컴포넌트들의 위치를 직접 지정해줄수 있다.
        
        pencil = new JButton("연필");
        pencil.setBackground(Color.WHITE); // 배경색 지정
        eraser = new JButton("지우개");
        eraser.setBackground(Color.WHITE); // 배경색 지정
        alleraser = new JButton("모두 지우기");
        alleraser.setBackground(Color.WHITE); // 배경색 지정
        color = new JButton("색 선택");
        color.setBackground(Color.WHITE); // 배경색 지정
        
        thicknessInfo = new JLabel("도구굵기"); 
        // 도구굴기 라벨 지정 / 밑에서 나올 텍스트필드의 역할을 설명
        thicknessInfo.setFont(new Font("함초롬돋움", Font.BOLD, 20));
        
        thicknessControl = new JTextField("5", 5); // 도구굵기 입력 텍스트필드 생성
        thicknessControl.setHorizontalAlignment(JTextField.CENTER); //텍스트 중앙 정렬
        thicknessControl.setFont(new Font("궁서체", Font.PLAIN, 25)); // 텍스트필드 X길이 및 폰트 지정
        
        pencil.setBounds(10,10,90,55); // 연필 버튼 위치 지정
        eraser.setBounds(105,10,109,55); // 지우개 버튼 위치 지정
        color.setBounds(785,10,90,55); // 선색상 버튼 위치 지정
        alleraser.setBounds(200,10,110,55); // 모두 지우기 버튼 위치 지정
        thicknessInfo.setBounds(640,10,100,55); // 도구굵기 라벨 위치 지정
        thicknessControl.setBounds(720,22,50,35); // 도구굵기 텍스트필드 위치 지정
        
        gui.add(pencil); // gui에 연필 버튼 추가
        gui.add(eraser); // gui에 지우개 버튼 추가
        gui.add(alleraser); // gui에 선색상 버튼 추가
        gui.add(color); // gui에 선색상 버튼 추가
        gui.add(thicknessInfo); // gui에 도구굵기 라벨 추가
        gui.add(thicknessControl); // gui에 도구굵기 텍스트필드 추가
        
        gui.setBounds(0,0,900,75); // gui_panel이 프레임에 배치될 위치 지정
        
        paint = new JPanel(); // 그림이 그려질 패널 생성
        paint.setBackground(Color.WHITE); // 패널의 배경색 하얀색
        paint.setLayout(null); // 패널 자체를 setBounds로 위치를 조정할수 있다.
        
        paint.setBounds(0,90,885,620); // paint_panel의 위치 조정
        
        add(gui); // 메인프레임에 gui패널 추가 - 위치는 위에서 다 정해줌
        add(paint); // 메인프레임에 paint패널 추가 - 위치는 위에서 다 정해줌
        
        setVisible(true); // 메인프레임을 보이게 한다.
        
        graphics = getGraphics(); // 그래픽초기화
        g = (Graphics2D)graphics; // Graphics2 는 인터넷 참조
        // 기존의 graphics변수를 Graphics2D로 변환후 Graphics2D에 초기화
        // 일반적인 Graphics가 아닌 Graphics2D를 사용한 이유는 펜의 굴기와 관련된 기능을
        // 수행하기 위하여 Graphics2D 클래스를 객체화함
        g.setColor(selectedColor); 
        // 그려질 선(=선도 그래픽)의 색상을 selectedColor의 값으로 설정
        
        paint.addMouseListener(new MouseListener() { 
            public void mousePressed(MouseEvent e) { 
                startX = e.getX(); // 마우스가 눌렸을때 그때의 X좌표값으로 초기화
                startY = e.getY(); // 마우스가 눌렸을때 그때의 Y좌표값으로 초기화
            }
            public void mouseClicked(MouseEvent e) {} // 클릭이벤트 처리
            public void mouseEntered(MouseEvent e) {} // paint_panel범위 내에 진입시 이벤트 처리
            public void mouseExited(MouseEvent e) {}
            public void mouseReleased(MouseEvent e) {}
        });
        paint.addMouseMotionListener(new PaintDraw());
          // paint에 마우스 모션리스너 추가
        pencil.addActionListener(new ToolActionListener()); // 연필버튼 액션처리
        eraser.addActionListener(new ToolActionListener()); // 지우개버튼 액션처리
        alleraser.addActionListener(new ToolActionListener()); // 모두지우기 버튼 액션처리
        color.addActionListener(new ActionListener() {
          // 선색상버튼 액션처리를 익명클래스로 작성
            public void actionPerformed(ActionEvent e) { // 오버라이딩
                tf = true; // 위에서 변수 설명을 했으므로 스킵..
                JColorChooser chooser = new JColorChooser(); // JColorChooser 클래스객체화
                selectedColor = chooser.showDialog(null, "Color", Color.BLUE); 
                // selectedColor에 선택된색으로 초기화
                g.setColor(selectedColor);// 그려지는 펜의 색상을 selectedColor를 매개변수로 하여 지정
            }
        });
    }
    
    public class PaintDraw implements MouseMotionListener {
        @Override
        public void mouseDragged(MouseEvent e) { 
            thickness = Integer.parseInt(thicknessControl.getText());
            // 텍스트필드부분에서 값을 값고와 thickness변수에 대입
                endX = e.getX(); 
                // 드래그 되는 시점에서 X좌표가 저장 - 밑에서 시작좌표와 끝좌표를 연결 해주어 선이 그어지게된다.
                endY = e.getY(); 
               // 드래그 되는 시점에서 Y좌표가 저장 - 밑에서 시작좌표와 끝좌표를 연결 해주어 선이 그어지게된다.
                g.setStroke(new BasicStroke(thickness, BasicStroke.CAP_ROUND,0)); //선굵기
                g.drawLine(startX+10, startY+121, endX+10, endY+121); // 라인이 그려지게 되는부분  
                startX = endX; 
                        // 시작부분이 마지막에 드래그된 X좌표로 찍혀야 다음에 이어 그려질수 있다.
                startY = endY;
                        // 시작부분이 마지막에 드래그된 Y좌표로 찍혀야 다음에 이어 그려질수 있다.
        }
        @Override
        public void mouseMoved(MouseEvent e) {}
    
    }
    
    public class ToolActionListener implements ActionListener {
        // 연필,지우개 버튼의 처리 클래스
        public void actionPerformed(ActionEvent e ) {
            if(e.getSource() == pencil) { // 
                if(tf == false) g.setColor(Color.BLACK); // 그려지는 색상을 검은색 지정
                else g.setColor(selectedColor);  // 그려지는 색상을 selectedColor변수의 값으로 지정
            } else if(e.getSource() == eraser) {
                g.setColor(Color.WHITE);
            // 그려지는 색을 흰색으로 처리해 지워지는것처럼 보이게함
            } else if(e.getSource() == alleraser) {
              paint.repaint();
            }
        }
    }
    
    public static void main(String[] args) { // 메인메소드
        new Paint();
    }
}