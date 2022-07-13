declare module "spawnjs/os" {
  type PlatformName = "windows" | "linux" | "mac" | "solaris" | "";

  const EOL: "\r\n" | "\n";

  const devNull: "\\\\.\\nul" | "/dev/null";

  function platform(): PlatformName;

  function endianness(): "BE" | "LE";

  function homedir(): string;
}
