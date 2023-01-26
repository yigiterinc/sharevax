import {createGlobalState} from 'react-hooks-global-state';

const {setGlobalState, useGlobalState} = createGlobalState({country: '', flag: ''});

export {setGlobalState, useGlobalState};
