package eu.iteije.clanwar.games.enums;

import eu.iteije.clanwar.ClanWar;
import eu.iteije.clanwar.games.objects.Game;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public enum GameEndCondition {

    TIME {
        int endValue = 0;
        int current = 0;

        @Override
        public void start(Game game, Integer endValue) {
            this.endValue = endValue;
            this.increment(new HashMap<>());
        }

        @Override
        public void increment(Map<Integer, Integer> values) {
            Bukkit.getScheduler().scheduleSyncRepeatingTask(JavaPlugin.getPlugin(ClanWar.class), () -> {
                current++;
                if (current >= endValue) {
                    // TODO end game and provide TIME as parameter
                }
            }, 20L, 20L);
        }

    },
    KILLS {
        int endValue = 0;
        final Map<Integer, Integer> current = new HashMap<>();

        @Override
        public void start(Game game, Integer endValue) {
            this.endValue = endValue;
        }

        @Override
        public void increment(Map<Integer, Integer> values) {
            for (int clanId : values.keySet()) {
                int incrementValue = values.get(clanId);
                int currentValue = current.getOrDefault(incrementValue, 0);
                currentValue += incrementValue;
                if (currentValue >= endValue) {
                    // TODO end game and add KILLS and the clanId as parameters
                }
                this.current.put(clanId, currentValue);
            }
        }
    },
    DAMAGE {
        int endValue = 0;
        final Map<Integer, Integer> current = new HashMap<>();

        @Override
        public void start(Game game, Integer endValue) {
            this.endValue = endValue;
        }

        @Override
        public void increment(Map<Integer, Integer> values) {
            for (int clanId : values.keySet()) {
                int incrementValue = values.get(clanId);
                int currentValue = current.getOrDefault(incrementValue, 0);
                currentValue += incrementValue;
                if (currentValue >= endValue) {
                    // TODO end game and add DAMAGE and the clanId as parameters
                }
                this.current.put(clanId, currentValue);
            }
        }
        
    }
    ;

    public abstract void start(Game game, Integer endValue);
    public abstract void increment(Map<Integer, Integer> values);

}
