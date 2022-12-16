import {createGlobalState} from 'react-hooks-global-state';

const {setGlobalState, useGlobalState} = createGlobalState({country: ''});

export {setGlobalState, useGlobalState};
