fn part1(input: &str) -> u64 {
    parse(input).into_iter().max().unwrap()
}

fn part2(input: &str) -> u64 {
    let mut inventories = parse(input);
    inventories.sort();
    inventories.into_iter().rev().take(3).sum::<u64>()
}

fn parse(input: &str) -> Vec<u64> {
    input
        .split("\n\n")
        .map(|elf| elf.lines().map(|l| l.parse::<u64>().unwrap()).sum())
        .collect()
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
        assert_eq!(part1(SAMPLE), 24000);
    }

    #[test]
    fn test_part_1() {
        assert_eq!(part1(INPUT), 71300);
    }

    #[test]
    fn test_part_2_sample() {
        assert_eq!(part2(SAMPLE), 45000);
    }

    #[test]
    fn test_part_2() {
        assert_eq!(part2(INPUT), 209691);
    }
}
