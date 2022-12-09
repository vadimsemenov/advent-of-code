fn part1(input: &str) -> u64 {
    parse(input)
        .iter()
        .filter_map(|rucksack| {
            assert_eq!(rucksack.len() % 2, 0);
            let (first, second) = rucksack.split_at(rucksack.len() / 2);
            first.iter().find(|&item| second.contains(item))
        })
        .sum()
}

fn part2(input: &str) -> u64 {
    let lines = parse(input);
    assert_eq!(lines.len() % 3, 0);
    (0..lines.len())
        .step_by(3)
        .filter_map(|i| {
            lines[i]
                .iter()
                .find(|&item| lines[i + 1].contains(item) && lines[i + 2].contains(item))
        })
        .sum()
}

fn parse(input: &str) -> Vec<Vec<u64>> {
    input
        .lines()
        .map(|line| line.bytes().map(decode).map(u64::from).collect())
        .collect()
}

fn decode(c: u8) -> u8 {
    match c {
        b'A'..=b'Z' => c - b'A' + 27,
        b'a'..=b'z' => c - b'a' + 1,
        _ => panic!(),
    }
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
        assert_eq!(part1(SAMPLE), 157);
    }

    #[test]
    fn test_part_1() {
        assert_eq!(part1(INPUT), 8109);
    }

    #[test]
    fn test_part_2_sample() {
        assert_eq!(part2(SAMPLE), 70);
    }

    #[test]
    fn test_part_2() {
        assert_eq!(part2(INPUT), 2738);
    }
}
