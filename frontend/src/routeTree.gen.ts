/* eslint-disable */

// @ts-nocheck

// noinspection JSUnusedGlobalSymbols

// This file was automatically generated by TanStack Router.
// You should NOT make any changes in this file as it will be overwritten.
// Additionally, you should also exclude this file from your linter and/or formatter to prevent it from being checked or modified.

import { Route as rootRouteImport } from './routes/__root'
import { Route as SetupRouteImport } from './routes/setup'
import { Route as LoginRouteImport } from './routes/login'
import { Route as GuideRouteImport } from './routes/guide'
import { Route as IndexRouteImport } from './routes/index'
import { Route as ProfileIndexRouteImport } from './routes/profile/index'
import { Route as ItemsIndexRouteImport } from './routes/items/index'
import { Route as ProfileEditRouteImport } from './routes/profile/edit'
import { Route as ProfileDeactivateRouteImport } from './routes/profile/deactivate'
import { Route as ItemsIdRouteImport } from './routes/items/$id'

const SetupRoute = SetupRouteImport.update({
  id: '/setup',
  path: '/setup',
  getParentRoute: () => rootRouteImport,
} as any)
const LoginRoute = LoginRouteImport.update({
  id: '/login',
  path: '/login',
  getParentRoute: () => rootRouteImport,
} as any)
const GuideRoute = GuideRouteImport.update({
  id: '/guide',
  path: '/guide',
  getParentRoute: () => rootRouteImport,
} as any)
const IndexRoute = IndexRouteImport.update({
  id: '/',
  path: '/',
  getParentRoute: () => rootRouteImport,
} as any)
const ProfileIndexRoute = ProfileIndexRouteImport.update({
  id: '/profile/',
  path: '/profile/',
  getParentRoute: () => rootRouteImport,
} as any)
const ItemsIndexRoute = ItemsIndexRouteImport.update({
  id: '/items/',
  path: '/items/',
  getParentRoute: () => rootRouteImport,
} as any)
const ProfileEditRoute = ProfileEditRouteImport.update({
  id: '/profile/edit',
  path: '/profile/edit',
  getParentRoute: () => rootRouteImport,
} as any)
const ProfileDeactivateRoute = ProfileDeactivateRouteImport.update({
  id: '/profile/deactivate',
  path: '/profile/deactivate',
  getParentRoute: () => rootRouteImport,
} as any)
const ItemsIdRoute = ItemsIdRouteImport.update({
  id: '/items/$id',
  path: '/items/$id',
  getParentRoute: () => rootRouteImport,
} as any)

export interface FileRoutesByFullPath {
  '/': typeof IndexRoute
  '/guide': typeof GuideRoute
  '/login': typeof LoginRoute
  '/setup': typeof SetupRoute
  '/items/$id': typeof ItemsIdRoute
  '/profile/deactivate': typeof ProfileDeactivateRoute
  '/profile/edit': typeof ProfileEditRoute
  '/items': typeof ItemsIndexRoute
  '/profile': typeof ProfileIndexRoute
}
export interface FileRoutesByTo {
  '/': typeof IndexRoute
  '/guide': typeof GuideRoute
  '/login': typeof LoginRoute
  '/setup': typeof SetupRoute
  '/items/$id': typeof ItemsIdRoute
  '/profile/deactivate': typeof ProfileDeactivateRoute
  '/profile/edit': typeof ProfileEditRoute
  '/items': typeof ItemsIndexRoute
  '/profile': typeof ProfileIndexRoute
}
export interface FileRoutesById {
  __root__: typeof rootRouteImport
  '/': typeof IndexRoute
  '/guide': typeof GuideRoute
  '/login': typeof LoginRoute
  '/setup': typeof SetupRoute
  '/items/$id': typeof ItemsIdRoute
  '/profile/deactivate': typeof ProfileDeactivateRoute
  '/profile/edit': typeof ProfileEditRoute
  '/items/': typeof ItemsIndexRoute
  '/profile/': typeof ProfileIndexRoute
}
export interface FileRouteTypes {
  fileRoutesByFullPath: FileRoutesByFullPath
  fullPaths:
    | '/'
    | '/guide'
    | '/login'
    | '/setup'
    | '/items/$id'
    | '/profile/deactivate'
    | '/profile/edit'
    | '/items'
    | '/profile'
  fileRoutesByTo: FileRoutesByTo
  to:
    | '/'
    | '/guide'
    | '/login'
    | '/setup'
    | '/items/$id'
    | '/profile/deactivate'
    | '/profile/edit'
    | '/items'
    | '/profile'
  id:
    | '__root__'
    | '/'
    | '/guide'
    | '/login'
    | '/setup'
    | '/items/$id'
    | '/profile/deactivate'
    | '/profile/edit'
    | '/items/'
    | '/profile/'
  fileRoutesById: FileRoutesById
}
export interface RootRouteChildren {
  IndexRoute: typeof IndexRoute
  GuideRoute: typeof GuideRoute
  LoginRoute: typeof LoginRoute
  SetupRoute: typeof SetupRoute
  ItemsIdRoute: typeof ItemsIdRoute
  ProfileDeactivateRoute: typeof ProfileDeactivateRoute
  ProfileEditRoute: typeof ProfileEditRoute
  ItemsIndexRoute: typeof ItemsIndexRoute
  ProfileIndexRoute: typeof ProfileIndexRoute
}

declare module '@tanstack/react-router' {
  interface FileRoutesByPath {
    '/setup': {
      id: '/setup'
      path: '/setup'
      fullPath: '/setup'
      preLoaderRoute: typeof SetupRouteImport
      parentRoute: typeof rootRouteImport
    }
    '/login': {
      id: '/login'
      path: '/login'
      fullPath: '/login'
      preLoaderRoute: typeof LoginRouteImport
      parentRoute: typeof rootRouteImport
    }
    '/guide': {
      id: '/guide'
      path: '/guide'
      fullPath: '/guide'
      preLoaderRoute: typeof GuideRouteImport
      parentRoute: typeof rootRouteImport
    }
    '/': {
      id: '/'
      path: '/'
      fullPath: '/'
      preLoaderRoute: typeof IndexRouteImport
      parentRoute: typeof rootRouteImport
    }
    '/profile/': {
      id: '/profile/'
      path: '/profile'
      fullPath: '/profile'
      preLoaderRoute: typeof ProfileIndexRouteImport
      parentRoute: typeof rootRouteImport
    }
    '/items/': {
      id: '/items/'
      path: '/items'
      fullPath: '/items'
      preLoaderRoute: typeof ItemsIndexRouteImport
      parentRoute: typeof rootRouteImport
    }
    '/profile/edit': {
      id: '/profile/edit'
      path: '/profile/edit'
      fullPath: '/profile/edit'
      preLoaderRoute: typeof ProfileEditRouteImport
      parentRoute: typeof rootRouteImport
    }
    '/profile/deactivate': {
      id: '/profile/deactivate'
      path: '/profile/deactivate'
      fullPath: '/profile/deactivate'
      preLoaderRoute: typeof ProfileDeactivateRouteImport
      parentRoute: typeof rootRouteImport
    }
    '/items/$id': {
      id: '/items/$id'
      path: '/items/$id'
      fullPath: '/items/$id'
      preLoaderRoute: typeof ItemsIdRouteImport
      parentRoute: typeof rootRouteImport
    }
  }
}

const rootRouteChildren: RootRouteChildren = {
  IndexRoute: IndexRoute,
  GuideRoute: GuideRoute,
  LoginRoute: LoginRoute,
  SetupRoute: SetupRoute,
  ItemsIdRoute: ItemsIdRoute,
  ProfileDeactivateRoute: ProfileDeactivateRoute,
  ProfileEditRoute: ProfileEditRoute,
  ItemsIndexRoute: ItemsIndexRoute,
  ProfileIndexRoute: ProfileIndexRoute,
}
export const routeTree = rootRouteImport
  ._addFileChildren(rootRouteChildren)
  ._addFileTypes<FileRouteTypes>()
