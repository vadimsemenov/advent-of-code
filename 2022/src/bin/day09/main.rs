use std::collections::HashSet;

fn part1(input: &str) -> usize {
    solve(input, 2)
}

fn part2(input: &str) -> usize {
    solve(input, 10)
}

fn solve(input: &str, rope_len: usize) -> usize {
    let mut rope = vec![(0, 0); rope_len];
    let mut visited = HashSet::<(i32, i32)>::new();
    visited.insert(rope[rope_len - 1]);
    for line in input.lines() {
        let (dir, count) = line.split_once(' ').unwrap();
        let (dx, dy) = match dir {
            "R" => (0, 1),
            "D" => (-1, 0),
            "L" => (0, -1),
            "U" => (1, 0),
            _ => panic!(),
        };
        let count = count.parse().unwrap();
        for _ in 0..count {
            rope[0].0 += dx;
            rope[0].1 += dy;
            for i in 0..rope_len - 1 {
                let h = rope[i];
                let t = &mut rope[i + 1];
                if (h.0 - t.0).abs() + (h.1 - t.1).abs() > 2 {
                    t.0 += (h.0 - t.0).signum();
                    t.1 += (h.1 - t.1).signum();
                } else if (h.0 - t.0).abs() > 1 {
                    t.0 += (h.0 - t.0).signum();
                } else if (h.1 - t.1).abs() > 1 {
                    t.1 += (h.1 - t.1).signum();
                }
            }
            visited.insert(rope[rope_len - 1]);
        }
    }
    visited.len()
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
        assert_eq!(part1(SAMPLE), 13);
    }

    #[test]
    fn test_part_1() {
        assert_eq!(part1(INPUT), 5960);
    }

    #[test]
    fn test_part_2_sample() {
        assert_eq!(part2(SAMPLE), 1);
    }

    #[test]
    fn test_part_2() {
        assert_eq!(part2(INPUT), 2327);
    }
}
