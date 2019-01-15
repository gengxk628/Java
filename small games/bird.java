import java.awt.*;
import java.awt.event.*;

public class bird {
    public static void main(String args[]) {
        myfr fr = new myfr("flying bird");
    }
}

class myfr extends Frame implements WindowListener {
    myca ca;

    myfr(String s) {
        super(s);
        ca = new myca();
        this.setLayout(new BorderLayout());
        this.add(ca, BorderLayout.CENTER);
        this.setSize(1000, 1000);
        this.addWindowListener(this);
        this.setVisible(true);
        ca.requestFocus();
    }

    public void windowDeactivated(WindowEvent e) {

    }

    public void windowActivated(WindowEvent e) {

    }

    public void windowDeiconified(WindowEvent e) {

    }

    public void windowIconified(WindowEvent e) {

    }

    public void windowClosed(WindowEvent e) {

    }

    public void windowClosing(WindowEvent e) {
        System.exit(0);
    }

    public void windowOpened(WindowEvent e) {

    }
}

class myca extends Canvas implements KeyListener, Runnable {
    mybird b;
    mypole p[];
    Thread clock;
    int interval = 30;
    boolean enabled = false;

    public void run() {
        while (enabled) {
            for (int i = 0; i < 4; i++) {
                p[i].move(5);
                b.addsum(p[i]);
            }
            b.move(b.v <= 12 ? b.v = b.v + b.g : b.v);
            Graphics g = getGraphics();
            g.setColor(Color.red);
            g.setFont(new Font("??", Font.BOLD, 16));
            g.drawString("You've got through " + b.sum + " glitches!", 5, 20);
            if (crack()) {
                stop();
                g.setColor(Color.black);
                g.setFont(new Font("??", Font.BOLD, 36));
                g.drawString("Press r key to retart!", 200, 400);
                break;
            }
            try {
                clock.sleep(interval);
            } catch (InterruptedException e) {

            }
            repaint();
        }
    }

    public boolean crack() {
        for (int i = 0; i < 4; i++)
            if (b.iscrack(p[i]))
                return true;
        return false;
    }

    myca() {
        super();
        this.addKeyListener(this);
        this.setBackground(Color.darkGray);
        Graphics g = getGraphics();
        // g.setColor(Color.black);
        // g.setFont(new Font("??", Font.BOLD, 36));
        // g.drawString("Press enter key to start!", 200, 400);
    }

    public void init() {
        clock = new Thread(this, "clock"); // çº¿ç¨‹
        b = new mybird(400, 400, 30, Color.red);
        p = new mypole[4];
        for (int i = 0; i < 4; i++) {
            p[i] = new mypole(Color.blue);
            p[i].x = 1000 + i * 250;
        }
        enabled = true;
        clock.start();
    }

    public void stop() {
        enabled = false;
    }

    public void paint(Graphics g) {
        g.setColor(b.color);
        g.fillOval(b.x - b.r, b.y - b.r, b.r + b.r, b.r + b.r);
        for (int i = 0; i < 4; i++) {
            g.setColor(p[i].color);
            g.fillRect(p[i].x, p[i].y, p[i].w, p[i].h);
        }
    }

    public void repaint() {
        Graphics g = this.getGraphics();
        update(g);
    }

    public void keyReleased(KeyEvent e) {

    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == e.VK_ENTER) {
            enabled = false;
            init();
        } else if (e.getKeyCode() == e.VK_SPACE) {
            b.click();
        } else if (e.getKeyCode() == e.VK_UP) {
            b.move(-250);
        } else if (e.getKeyCode() == e.VK_DOWN) {
            b.move(250);
        } else if (e.getKeyCode() == e.VK_LEFT) {
            for (int i = 0; i < 4; i++)
                p[i].move(-250);
        } else if (e.getKeyCode() == e.VK_RIGHT) {
            for (int i = 0; i < 4; i++)
                p[i].move(250);
        } else if (e.getKeyCode() == e.VK_R) {
            init();
        }
    }

    public void keyTyped(KeyEvent e) {

    }

    public void keyEntered(KeyEvent e) {

    }

}

class mybird {
    public static int g = 1;
    public int x = 0, y = 0, r = 0;
    public int v = 0;
    public Color color = Color.white;
    int sum = 0;

    mybird(int x, int y, int r, Color color) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.color = color;
    }

    public void move(int b) {
        y += b;
    }

    public void click() {
        v = 0;
        y -= 20;
        if (v <= 2)
            y -= 50;
    }

    public boolean iscrack(mypole pole) {
        if (x + r >= pole.x && x - r <= pole.x + pole.w && y + r >= pole.y && y - r <= pole.y + pole.h)
            return true;
        else
            return false;
    }

    public void addsum(mypole p) {
        if (!p.isby && p.x <= x) {
            sum++;
            p.isby = true;
        }
    }
}

class mypole {
    public int x = 0, y = 0, w = 0, h = 0;
    public Color color = Color.white;
    boolean isby = false;

    mypole(Color color) {
        this.x = 1000;
        this.w = 60;
        this.h = (int) (Math.random() * 800);
        this.y = Math.random() > 0.5 ? 0 : 1000 - this.h;
        this.color = color;
    }

    public void move(int a) {
        x -= a;
        if (isin() == false) {
            x = 1000 + x;
            this.h = (int) (Math.random() * 800);
            this.y = Math.random() > 0.5 ? 0 : 1000 - this.h;
            this.isby = false;
        }
    }

    public boolean isin() {
        if (x + w <= 0)
            return false;
        else
            return true;
    }
}
