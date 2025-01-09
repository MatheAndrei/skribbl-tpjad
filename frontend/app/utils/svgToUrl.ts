export function svgToUrl(svg: string): string {
    return "data:image/svg+xml;base64," + btoa(svg);
}