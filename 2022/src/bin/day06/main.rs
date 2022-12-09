use std::collections::HashSet;

fn part1(input: &str) -> usize {
    solve(input, 4)
}

fn part2(input: &str) -> usize {
    solve(input, 14)
}

fn solve(input: &str, window: usize) -> usize {
    let input = Vec::from(input);
    input
        .windows(window)
        .position(|w| w.iter().collect::<HashSet<_>>().len() == window)
        .unwrap()
        + window
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
        assert_eq!(part1(SAMPLE), 7);
    }

    #[test]
    fn test_part_1() {
        assert_eq!(part1(INPUT), 1275);
    }

    #[test]
    fn test_part_2_sample() {
        assert_eq!(part2(SAMPLE), 19);
    }

    #[test]
    fn test_part_2() {
        assert_eq!(part2(INPUT), 3605);
    }
}
