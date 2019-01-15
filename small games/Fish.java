import java.awt.*;
import java.awt.event.*;

public class Fish {
    public static void main(String args[]) {
        MyFrame fr = new MyFrame("fish");
    }
}

class MyFish {
    int v = 1;
    int r = 18;
    Color color = new Color(255, 200, 0);
    int x = 500, y = 500;
    int vx = 0, vy = 0;
    int sum = 0;

    public boolean isin() {
        if (x - r > 1000 || y - r > 1000 || x + r < 0 || y + r < 0)
            return false;
        return true;
    }

    public void move() {
        x += vx;
        y += vy;
    }

}

class OtherFish {
    int r;
    Color color;
    int x, y;
    int vx, vy;

    OtherFish(int r, Color color, int x, int y, int vx, int vy) {
        this.r = r;
        this.color = color;
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
    }

    public boolean iscrack(MyFish f) {
        if ((x - f.x) * (x - f.x) + (y - f.y) * (y - f.y) <= (r + f.r) * (r + f.r))
            return true;
        return false;
    }

    public boolean isin() {
        if (x - r > 1000 || y - r > 1000 || x + r < 0 || y + r < 0)
            return false;
        return true;
    }

    public void move() {
        x += vx;
        y += vy;
    }
}

class MyCanvas extends Canvas implements Runnable, KeyListener, MouseListener {

    boolean enabled = false;
    int interval = 30;
    Thread clock;
    MyFish f;
    OtherFish h[];

    public synchronized void start() {
        enabled = true;
        run();
    }

    public void run() {
        while (enabled) {
            work();
            try {
                clock.sleep(interval);
            } catch (InterruptedException e) {

            }
        }
    }

    public void work() {//////////////////////////////// 主执行体
        for (int i = 1; i < 20; i++) {
            f.move();
            h[i].move();
            if (!h[i].isin()) {
                setFish(i);
            }
            if (h[i].iscrack(f)) {
                if (f.r >= h[i].r) {
                    f.r += (int) (0.2 * (Math.sqrt((double) (h[i].r * h[i].r + f.r * f.r)) - (double) f.r)) + 1;
                    setFish(i);
                    f.sum++;
                    if (f.r >= 100) {
                        enabled = false;
                        Graphics g = this.getGraphics();
                        g.setColor(new Color(0, 0, 0));
                        Font font = new Font("宋体", Font.BOLD, 36);
                        g.setFont(font);
                        g.drawString("You becomes the largest one!", 200, 400);
                        g.drawString("Click twice to restart!", 200, 600);
                        return;
                    }
                } else {
                    enabled = false;
                    Graphics g = this.getGraphics();
                    g.setColor(new Color(0, 0, 0));
                    Font font = new Font("宋体", Font.BOLD, 36);
                    g.setFont(font);
                    g.drawString("You are eaten by a larger one!", 200, 400);
                    g.drawString("Click twice to restart!", 200, 600);
                    return;
                }
            }
        }
        // f.move(5, 5);
        repaint();
    }

    public synchronized void stop() {
        enabled = false;
    }

    MyCanvas() {
        super();
        this.setBackground(new Color(100, 150, 240));
        this.addMouseListener(this);
        this.addKeyListener(this);
        init();
    }

    public void init() {
        f = new MyFish();
        h = new OtherFish[20];
        /////////////////////////////////////////////// set the initial shuxing
        for (int i = 0; i < 20; i++) {
            setFish(i);
            // int cx = (int) (Math.random() * 1000);
            // int cy = (int) (Math.random() * 1000);
            // h[i] = new OtherFish((int) Math.random() * 200,
            // new Color((int) (Math.random() * 254), (int) (Math.random() * 254), (int)
            // (Math.random() * 254)),
            // cx, cy, (int) (Math.random() * 20 - 10), (int) (Math.random() * 20 - 10));
        }

        clock = new Thread(this);
        enabled = true;
        clock.start();
        // start();
    }

    private void setFish(int index) {
        int cr = (int) (Math.random() * 100);
        int cx, cy, cvx, cvy;
        int v = 6;////////////////////////////////// the quickest speed
        double rnd = Math.random();
        if (rnd > 0.75) {
            cx = 1000 + cr;
            cy = (int) (Math.random() * 1000);
            cvx = (int) (-Math.random() * v);
            cvy = (int) (Math.random() * 2 * v - v);
        } else if (rnd > 0.5) {
            cx = -cr;
            cy = (int) (Math.random() * 1000);
            cvx = (int) (Math.random() * v);
            cvy = (int) (Math.random() * 2 * v - v);
        } else if (rnd > 0.25) {
            cy = 1000 + cr;
            cx = (int) (Math.random() * 1000);
            cvy = (int) (-Math.random() * v);
            cvx = (int) (Math.random() * 2 * v - v);
        } else {
            cy = -cr;
            cx = (int) (Math.random() * 1000);
            cvy = (int) (Math.random() * v);
            cvx = (int) (Math.random() * 2 * v - v);
        }
        h[index] = new OtherFish(cr,
                new Color((int) (Math.random() * 254), (int) (Math.random() * 254), (int) (Math.random() * 254)), cx,
                cy, cvx, cvy);
    }

    public void paint(Graphics g) {
        g.setColor(f.color);
        g.fillOval(f.x - f.r, f.y - f.r, f.r + f.r, f.r + f.r);
        for (int i = 0; i < 20; i++) {
            g.setColor(h[i].color);
            g.fillOval(h[i].x - h[i].r, h[i].y - h[i].r, h[i].r + h[i].r, h[i].r + h[i].r);
        }
        g.setFont(new Font("宋体", Font.BOLD, 16));
        g.setColor(Color.black);
        g.drawString("Your current size is " + f.r * f.r + " , and till now you've eaten " + f.sum + " other fish", 5,
                30);
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == e.VK_RIGHT) {
            f.vx = f.v;
        } else if (e.getKeyCode() == e.VK_LEFT) {
            f.vx = -f.v;
        } else if (e.getKeyCode() == e.VK_UP) {
            f.vy = -f.v;
        } else if (e.getKeyCode() == e.VK_DOWN) {
            f.vy = f.v;
        }
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == e.VK_LEFT || e.getKeyCode() == e.VK_RIGHT) {
            f.vx = 0;
        } else if (e.getKeyCode() == e.VK_UP || e.getKeyCode() == e.VK_DOWN) {
            f.vy = 0;
        }
    }

    public void keyTyped(KeyEvent e) {

    }

    public void keyEntered(KeyEvent e) {

    }

    public void mousePressed(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2)
            init();
    }

    public void mouseReleased(MouseEvent e) {

    }
}

class MyFrame extends Frame implements WindowListener {
    MyCanvas ca;

    MyFrame(String s) {
        super(s);
        ca = new MyCanvas();
        this.setSize(1000, 1000);
        this.setLayout(new BorderLayout());
        this.addWindowListener(this);
        this.add(ca, BorderLayout.CENTER);
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
