use std::cmp::{max, min};
use std::collections::HashMap;

fn part1(input: &str) -> i32 {
    return *dp(input, 30).last().unwrap();
}

fn part2(input: &str) -> i32 {
    let can_release = dp(input, 26);
    let total_mask = can_release.len() - 1;
    let mut ans = 0;
    for my_mask in 0..=total_mask {
        let elephant_mask = total_mask ^ my_mask;
        ans = max(ans, can_release[my_mask] + can_release[elephant_mask]);
    }
    return ans;
}

fn dp(input: &str, time_limit: usize) -> Vec<i32> {
    let mut index_by_name = HashMap::new();
    let mut neighbours: HashMap<&str, Vec<&str>> = HashMap::new();
    let mut global_index = vec![];
    let mut flow_rate = vec![];
    for line in input.lines() {
        let (valve, description) = line
            .strip_prefix("Valve ")
            .unwrap()
            .split_once(" has flow rate=")
            .unwrap();
        let index = index_by_name.len();
        index_by_name.insert(valve, index);
        let (rate, tunnels) =
            if let Some((rate, tunnels)) = description.split_once("; tunnels lead to valves ") {
                (rate, tunnels)
            } else {
                description.split_once("; tunnel leads to valve ").unwrap()
            };
        let rate = rate.parse::<i32>().unwrap();
        neighbours.insert(valve, tunnels.split(", ").collect());
        if rate != 0 {
            global_index.push(index);
            flow_rate.push(rate);
        }
    }
    let total_valves = index_by_name.len();
    let mut global_distances = vec![vec![total_valves + 1; total_valves]; total_valves];
    for (v, neighbours) in &neighbours {
        for u in neighbours {
            global_distances[index_by_name[v]][index_by_name[u]] = 1;
        }
    }
    for k in 0..total_valves {
        for i in 0..total_valves {
            for j in 0..total_valves {
                global_distances[i][j] = min(
                    global_distances[i][j],
                    global_distances[i][k] + global_distances[k][j],
                );
            }
        }
    }

    let non_empty_valves = global_index.len();
    let mut distances = vec![vec![total_valves + 1; non_empty_valves]; non_empty_valves];
    for v in 0..non_empty_valves {
        for u in 0..non_empty_valves {
            distances[v][u] =
                global_distances[global_index[v]][global_index[u]];
        }
    }

    // dp[remaining time][current position][unvisited mask] = can release
    let total_mask = (1 << non_empty_valves) - 1;
    let mut dp = vec![vec![vec![0; total_mask + 1]; non_empty_valves]; time_limit + 1];
    for time in 1..=time_limit {
        for cur in 0..non_empty_valves {
            for closed_mask in 0..dp[time][cur].len() {
                if ((closed_mask >> cur) & 1) == 0 && time + 1 <= time_limit {
                    let prev_closed = closed_mask ^ (1 << cur);
                    let released = time as i32 * flow_rate[cur];
                    dp[time + 1][cur][prev_closed] = max(
                        dp[time + 1][cur][prev_closed],
                        released + dp[time][cur][closed_mask],
                    );
                } else {
                    let mut opened_mask = total_mask ^ closed_mask;
                    while opened_mask > 0 {
                        let prev = opened_mask.trailing_zeros() as usize;
                        let dist = distances[prev][cur];
                        if dist + time <= time_limit {
                            dp[time + dist][prev][closed_mask] = max(
                                dp[time + dist][prev][closed_mask],
                                dp[time][cur][closed_mask],
                            );
                        }
                        opened_mask = opened_mask ^ (1 << prev);
                    }
                }
            }
        }
    }

    let mut can_release = vec![0; total_mask + 1];
    let dist_from_aa = &global_distances[index_by_name["AA"]];
    for v in 0..non_empty_valves {
        let dist = dist_from_aa[global_index[v]];
        for mask in 0..=total_mask {
            can_release[mask] = max(can_release[mask], dp[time_limit - dist][v][mask]);
        }
    }

    can_release
}

const INPUT: &str = include_str!("input.txt");
fn main() {
    println!("{}", part1(INPUT));
    println!("{}", part2(INPUT));
}

#[cfg(test)]
mod tests {
    use super::*;
    const SAMPLE: &str = include_str!("sample.txt");

    #[test]
    fn test_part_1_sample() {
        assert_eq!(part1(SAMPLE), 1651);
    }

    #[test]
    fn test_part_1() {
        assert_eq!(part1(INPUT), 1741);
    }

    #[test]
    fn test_part_2_sample() {
        assert_eq!(part2(SAMPLE), 1707);
    }

    #[test]
    fn test_part_2() {
        assert_eq!(part2(INPUT), 2316);
    }
}
