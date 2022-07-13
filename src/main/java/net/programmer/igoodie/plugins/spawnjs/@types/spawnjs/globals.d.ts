declare global {
  const __context: Function;
  const __importedLibs: Record<string, Record<string, any>>;
  const __dumpscope: Function;
  const __scriptdir: string;
  const __scriptfilename: string;

  const $TSL_VERSION: string;

  function require<M extends SpawnJSModule>(moduleName: string): M;
  function print(...args: any[]): void;

  const eventName: string;
  const event: Record<string, any>;
}

export {};

interface SpawnJSModule {
  default?: any;
  [key: string]: any;
}
