package Framework.PlayerEffects;

import Setup.Main;

public abstract class PlayerEffect {
    int maxTime, cTime; // cTime starter ved max og går ned
    String effectName;

    protected PlayerEffect(int maxTime, String effectName) {
        this.maxTime = maxTime;
        cTime = maxTime;
        this.effectName = effectName;
    }

    public void apply() {
        effect();

        cTime--;
        if (cTime <= 0)
            end();
    }

    abstract void effect();

    abstract void endEffect();

    void end() {
        Main.player.removePlayerEffect(this);
        endEffect();
    }

    protected abstract void drawEffectSymbol(int x, int y);

    public void drawOnUI(int x, int y) {
        // RECTANGLE
        Main.main.fill(50);
        Main.main.strokeWeight(2);
        Main.main.stroke(0);
        Main.main.rect(x, y, 200, 60);

        // TEXT
        Main.main.noStroke();
        Main.main.fill(255);
        Main.main.textSize(20);
        Main.main.text(effectName, x + 10, y + 25);

        // PROGRESSBAR
        Main.main.fill(255);
        Main.main.rect(x + 10, y + 30, 180 * (maxTime - cTime) / maxTime, 20);

        Main.main.noFill();
        Main.main.stroke(0);
        Main.main.rect(x + 10, y + 30, 180, 20);

        drawEffectSymbol(x, y);

    }

    protected void drawProgressBar(int x, int y) {
        Main.main.fill(255);

        // Main.main.rect(x+60,)
    }
}
