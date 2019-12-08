import { NativeModules } from 'react-native'

const { DynamicBundleLoader } = NativeModules

export function setActiveBundle(bundleId) {
  DynamicBundleLoader.setActiveBundle(bundleId)
}

/**
 * Register a Javascript bundle in the bundle registry. The path to the bundle
 * should be relative to the documents directory on iOS and to the internal app
 * storage directory on Android, i.e. the directory returned by `getFilesDir()`.
 */
export function registerBundle(bundleId, relativePath) {
  DynamicBundleLoader.registerBundle(bundleId, relativePath)
}

/**
 * Unregister a bundle from the bundle registry.
 */
export function unregisterBundle(bundleId) {
  DynamicBundleLoader.unregisterBundle(bundleId)
}

/**
 * Reload the bundle that is used by the app immediately. This can be used to
 * apply a new bundle that was set by `setActiveBundle()` immediately.
 */
export function reloadBundle() {
  DynamicBundleLoader.reloadBundle()
}

/**
 * Returns a promise that resolves to an object with the contents of the bundle
 * registry, where the keys are the bundle identifiers and values are the
 * bundle locations encoded as a file URL.
 */
export function getBundles() {
  return DynamicBundleLoader.getBundles()
}

/**
 * Returns a promise that resolves to the currently active bundle identifier.
 * if the default bundle (i.e. the bundle that was packaged into the native app)
 * is active this method will resolve to `null`.
 */
export function getActiveBundle() {
  return DynamicBundleLoader.getActiveBundle()
}
