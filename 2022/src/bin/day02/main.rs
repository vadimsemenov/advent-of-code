fn part1(input: &str) -> u64 {
    solve(input, |their, my| (my + 1 + (my + (3 - their) + 1) % 3 * 3))
}

fn part2(input: &str) -> u64 {
    solve(input, |their, result| {
        (result * 3 + (their + result + 2) % 3 + 1)
    })
}

fn solve<F>(input: &str, f: F) -> u64
where
    F: Fn(u8, u8) -> u8,
{
    input
        .lines()
        .map(|l| {
            let bytes: Vec<u8> = l.bytes().collect();
            assert_eq!(bytes.len(), 3);
            f(bytes[0] - b'A', bytes[2] - b'X') as u64
        })
        .sum()
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
        assert_eq!(part1(SAMPLE), 15);
    }

    #[test]
    fn test_part_1() {
        assert_eq!(part1(INPUT), 8392);
    }

    #[test]
    fn test_part_2_sample() {
        assert_eq!(part2(SAMPLE), 12);
    }

    #[test]
    fn test_part_2() {
        assert_eq!(part2(INPUT), 10116);
    }
}
