import { contextBridge } from 'electron'
import { electronAPI } from '@electron-toolkit/preload'
import { existsSync, readFileSync, writeFileSync } from 'fs'
import { resolve } from "path";

let data: any[] = []

// Custom APIs for renderer
const api = {
  getDatabase: () => data,
  setDatabase: (user: any) => {
    const temp = [...data, user]
    const path = resolve(__dirname, 'database.json')
    console.log(process.cwd())
    console.log('before', ...data)
    console.log('after', ...temp)
    writeFileSync(path, JSON.stringify(temp))
    data = temp
  }
}

// Use `contextBridge` APIs to expose Electron APIs to
// renderer only if context isolation is enabled, otherwise
// just add to the DOM global.
if (process.contextIsolated) {
  try {
    contextBridge.exposeInMainWorld('electron', electronAPI)
    if (existsSync(resolve(__dirname, 'database.json'))) {
      data = JSON.parse(readFileSync(resolve(__dirname, 'database.json'), 'utf-8'))
    } else {
      writeFileSync(resolve(__dirname, 'database.json'), JSON.stringify(data))
    }
    contextBridge.exposeInMainWorld('api', api)
  } catch (error) {
    console.error(error)
  }
} else {
  // @ts-ignore (define in dts)
  window.electron = electronAPI
  // @ts-ignore (define in dts)
  window.api = api
}
