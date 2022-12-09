use std::cmp::{max, min};

fn part1(input: &str) -> usize {
    input
        .lines()
        .map(parse)
        .filter(|(fst, snd)| {
            let enclosing = &(min(fst.0, snd.0), max(fst.1, snd.1));
            fst == enclosing || snd == enclosing
        })
        .count()
}

fn part2(input: &str) -> usize {
    input
        .lines()
        .map(parse)
        .filter(|(fst, snd)| max(fst.0, snd.0) <= min(fst.1, snd.1))
        .count()
}

fn parse(l: &str) -> ((u64, u64), (u64, u64)) {
    let vec = l
        .split(&['-', ','])
        .flat_map(|id| id.parse::<u64>())
        .collect::<Vec<_>>();
    assert_eq!(vec.len(), 4);
    ((vec[0], vec[1]), (vec[2], vec[3]))
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
        assert_eq!(part1(SAMPLE), 2);
    }

    #[test]
    fn test_part_1() {
        assert_eq!(part1(INPUT), 602);
    }

    #[test]
    fn test_part_2_sample() {
        assert_eq!(part2(SAMPLE), 4);
    }

    #[test]
    fn test_part_2() {
        assert_eq!(part2(INPUT), 891);
    }
}
