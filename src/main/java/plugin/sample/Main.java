package plugin.sample;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.joml.Math;
import org.joml.Random;

public final class Main extends JavaPlugin implements Listener, CommandExecutor {

  private int count;

  public static boolean isPrime(int n) {
    if (n <= 1) {
      return false;
    }
    for (int i = 2; i <= Math.sqrt(n); i++) {
      if (n % i == 0) {
        return false;
      }
    }
    return true;
  }

  //  @Override
  public void onEnable() {

    Bukkit.getPluginManager().registerEvents(this, this);
    getCommand("fireworks").setExecutor(this);
  }

  /**
   * プレイヤーがスニークを開始/終了する際に起動されるイベントハンドラ。
   *
   * @param e イベント
   */
  @EventHandler
  public void onPlayerToggleSneak(PlayerToggleSneakEvent e) throws IOException {
    // イベント発生時のプレイヤーやワールドなどの情報を変数に持つ。
    Player player = e.getPlayer();
    World world = player.getWorld();
    // 花火に設定するsetPowerを乱数に応じて変化させる。
    Random random = new Random();
    // 3以上を設定すると花火が遠くに飛びすぎるため0~2の乱数値を設定する。
    int fireworksPowerAmount = random.nextInt(3);
    List<Color> colorList = List.of(Color.RED, Color.BLUE, Color.GRAY, Color.YELLOW);

    //素数の場合は花火を打ち上げてチャットにメッセージを書き込む
    if (count % 2 == 0) {
      for (Color color : colorList) {
        // 花火オブジェクトをプレイヤーのロケーション地点に対して出現させる。
        Firework firework = world.spawn(player.getLocation(), Firework.class);

        // 花火オブジェクトが持つメタ情報を取得。
        FireworkMeta fireworkMeta = firework.getFireworkMeta();

        // メタ情報に対して設定を追加したり、値の上書きを行う。
        // 今回は青色で星型の花火を打ち上げる。
        fireworkMeta.addEffect(
            FireworkEffect.builder()
                .withColor(color)
                .with(Type.STAR)
                .withFlicker()
                .build());
        fireworkMeta.setPower(fireworksPowerAmount);

        // 追加した情報で再設定する。
        firework.setFireworkMeta(fireworkMeta);
      }
      //ファイル書き込み
      Path path = Path.of("firework.txt");
      Files.writeString(path, "たーまやー\n", StandardOpenOption.APPEND);
      Files.writeString(path, colorList.size() + "色の花火を打ち上げました。", StandardOpenOption.APPEND);
      player.sendMessage(Files.readString(path));
//      player.sendMessage(colorList.size() + "色の花火を打ち上げました。");

    }
    count++;
  }

  // コマンド実行時の挙動を制御する
  @Override
  public boolean onCommand(CommandSender sender, Command command, String Label, String[] args) {

    List<Color> colorList = List.of(Color.RED, Color.BLUE, Color.GRAY, Color.YELLOW);

    if (command.getName().equalsIgnoreCase("fireworks")) {
      Player player = (Player) sender;
      World world = player.getWorld();
      player.sendMessage("花火コマンドを実行しました");
      for (Color color : colorList) {
        // 花火オブジェクトをプレイヤーのロケーション地点に対して出現させる。
        Firework firework = world.spawn(player.getLocation(), Firework.class);

        // 花火オブジェクトが持つメタ情報を取得。
        FireworkMeta fireworkMeta = firework.getFireworkMeta();

        // メタ情報に対して設定を追加したり、値の上書きを行う。
        // 今回は青色で星型の花火を打ち上げる。
        fireworkMeta.addEffect(
            FireworkEffect.builder()
                .withColor(color)
                .with(Type.STAR)
                .withFlicker()
                .build());
        fireworkMeta.setPower(0);

        // 追加した情報で再設定する。
        firework.setFireworkMeta(fireworkMeta);
      }
      return true;
    }
    return false;

  }

  @EventHandler
  public void OnPlayerBedEnter(PlayerBedEnterEvent e) {
    Player player = e.getPlayer();
    ItemStack[] itemStacks = player.getInventory().getContents();
    Arrays.stream(itemStacks)
        .filter(item -> !Objects.isNull(item) && item.getMaxStackSize() != item.getAmount())
        .forEach(item -> item.setAmount(0));

    player.getInventory().setContents(itemStacks);
  }
}




