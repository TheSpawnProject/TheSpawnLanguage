/// <reference path="globals.d.ts"/>

/// <reference path="os.d.ts"/>
type OsModule = typeof import("spawnjs/os");

// Default Module
declare module "spawnjs" {
  const os: OsModule;
}

/* ------------------------------ */

// Allow dynamic import of modules
// which do not expose any d.ts
declare module "*" {
  const x: any;
  export = x;
}
