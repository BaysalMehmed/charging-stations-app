import React, { Component } from 'react'
import FormGroup from '@material-ui/core/FormGroup';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import Switch from '@material-ui/core/Switch';
import IMapFilter from '../interface/IMapFilter';

class MapFilter extends Component<IMapFilter> {

    render() {

        const { chargerFilters, handleFilterToggle} = this.props

        return (
            <FormGroup>
            {chargerFilters.map(e => {
                return <FormControlLabel key={e.type.toString() + "label"}
                control={<Switch key={e.type.toString() + "switch"} checked={e.showType} onChange={() => handleFilterToggle(e.type)}/>}
                label={e.type}/>
            })}
            </FormGroup>
        )
    }
}

export default MapFilter